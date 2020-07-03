<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>申请会议室</el-breadcrumb-item>
            <el-breadcrumb-item>取消预约</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图 -->
        <el-card>
            <el-table :data="applicationList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="申请部门" prop="deptName"></el-table-column>
                <el-table-column label="会议室" prop="roomNo"></el-table-column>
                <el-table-column label="会议主题" prop="meetingTheme"></el-table-column>
                <el-table-column label="开会日期">
                    <template slot-scope="scope">
                        <div>
                            {{ $moment(scope.row.applyDate).format('YYYY/MM/DD') }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="开会时间">
                    <template slot-scope="scope">
                        <div v-if="scope.row.applySlot == 1">9:00-10:30</div>
                        <div v-if="scope.row.applySlot == 2">10:30-12:00</div>
                        <div v-if="scope.row.applySlot == 3">13:00-15:00</div>
                        <div v-if="scope.row.applySlot == 4">15:00-17:00</div>
                        <div v-if="scope.row.applySlot == 5">17:00-19:00</div>
                    </template>
                </el-table-column>
                <el-table-column label="申请日期">
                    <template slot-scope="scope">
                        <div>
                            {{ $moment(scope.row.applyTime).format('YYYY/MM/DD') }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <!-- 删除按钮 -->
                        <el-tooltip class="item" effect="dark" content="取消预约" placement="top" :enterable="false">
                            <el-button type="danger" size="mini" icon="el-icon-delete" @click="remove(scope.row.applicationId)"></el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页区域 -->
            <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.currentPage"
            :page-sizes="[2, 4, 8, 10]"
            :page-size="queryInfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </el-card>
    </div>
</template>

<script>
export default {
    data () {
        return {
            applicationList: [],
            queryInfo: {
                pageSize: 4,
                currentPage: 1,
                auditStatus: 0
            },
            total: 0
        }
    },
    created () {
        this.getList()
    },
    methods: {
        async getList () {
            const { data: res } = await this.$http.get('/apply/history', { params: this.queryInfo })
            if (res.code !== 100) {
                return this.$message.error('获取申请列表失败！')
            }
            this.applicationList = res.extend.result.list
            this.total = res.extend.result.total
            console.log(res)
        },
        handleSizeChange (newSize) {
            this.queryInfo.pageSize = newSize
            this.getList()
        },
        handleCurrentChange (newPage) {
            this.queryInfo.currentPage = newPage
            this.getList()
        },
        async remove (id) {
            const confirmResult = await this.$confirm('此操作将删除本次预约, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (confirmResult !== 'confirm') {
                return this.$message.info('已取消删除')
            }
            const { data: res } = await this.$http.delete('/apply/revoke', { data: { applicationId: id } })
            if (res.code !== 100) {
                return this.$message.error('取消预约失败！')
            }
            this.$message.success('取消预约成功！')
            this.getList()
        }
    }
}
</script>
