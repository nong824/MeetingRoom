<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>审批申请</el-breadcrumb-item>
            <el-breadcrumb-item>已审批</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card>
            <!-- tab标签页 -->
            <el-tabs v-model="activeName">
                <el-tab-pane label="批准的审批" name="accept">
                    <el-table :data="acceptList" border stripe>
                        <el-table-column type="index"></el-table-column>
                        <el-table-column label="申请部门" prop="deptName"></el-table-column>
                        <el-table-column label="会议室" prop="roomNo"></el-table-column>
                        <el-table-column label="参会人数" prop="meetingSize"></el-table-column>
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
                    </el-table>

                    <!-- 分页区域 -->
                    <el-pagination
                    @size-change="handleSizeChangeAccept"
                    @current-change="handleCurrentChangeAccept"
                    :current-page="acceptInfo.currentPage"
                    :page-sizes="[5, 10]"
                    :page-size="acceptInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="acceptTotal">
                    </el-pagination>
                </el-tab-pane>
                <el-tab-pane label="拒绝的审批" name="reject">
                    <el-table :data="rejectList" border stripe>
                        <el-table-column type="index"></el-table-column>
                        <el-table-column label="申请部门" prop="deptName" width="85"></el-table-column>
                        <el-table-column label="会议室" prop="roomNo"></el-table-column>
                        <el-table-column label="参会人数" prop="meetingSize"></el-table-column>
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
                        <el-table-column label="拒绝理由" prop="rejectReason"></el-table-column>
                    </el-table>

                    <!-- 分页区域 -->
                    <el-pagination
                    @size-change="handleSizeChangeNotReject"
                    @current-change="handleCurrentChangeNotReject"
                    :current-page="rejectInfo.currentPage"
                    :page-sizes="[5, 10]"
                    :page-size="rejectInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="rejectTotal">
                    </el-pagination>
                </el-tab-pane>
            </el-tabs>
        </el-card>
    </div>
</template>

<script>
export default {
    data () {
        return {
            activeName: 'accept',
            acceptList: [],
            rejectList: [],
            acceptInfo: {
                auditStatus: 1,
                pageSize: 5,
                currentPage: 1
            },
            rejectInfo: {
                auditStatus: 2,
                pageSize: 5,
                currentPage: 1
            },
            acceptTotal: 0,
            rejectTotal: 0
        }
    },
    created () {
        this.getAcceptList()
        this.getRejectList()
    },
    methods: {
        async getAcceptList () {
            const { data: res } = await this.$http.get('/apply/history', { params: this.acceptInfo })
            if (res.code !== 100) {
                return this.$message.error('获取申请列表失败！')
            } else {
                this.acceptList = res.extend.result.list
                this.acceptTotal = res.extend.result.total
            }
        },
        async getRejectList () {
            const { data: res } = await this.$http.get('/apply/history', { params: this.rejectInfo })
            if (res.code !== 100) {
                return this.$message.error('获取申请列表失败！')
            } else {
                this.rejectList = res.extend.result.list
                this.rejectTotal = res.extend.result.total
            }
        },
        handleSizeChangeAccept (newSize) {
            this.acceptInfo.pageSize = newSize
            this.getAcceptList()
        },
        handleCurrentChangeAccept (newPage) {
            this.acceptInfo.currentPage = newPage
            this.getAcceptList()
        },
        handleSizeChangeNotReject (newSize) {
            this.rejectInfo.pageSize = newSize
            this.getRejectList()
        },
        handleCurrentChangeNotReject (newPage) {
            this.rejectInfo.currentPage = newPage
            this.getRejectList()
        }
    }
}
</script>
