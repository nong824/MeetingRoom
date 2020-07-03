<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>预约进程/历史</el-breadcrumb-item>
            <el-breadcrumb-item>查看历史会议</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图 -->
        <el-card>
            <!-- 头部警告区域 -->
            <el-alert show-icon :closable="false" title="注意：只有通过审批的会议才会加入到历史会议中！" type="warning"></el-alert>

            <!-- tab标签页 -->
            <el-tabs v-model="activeName">
                <el-tab-pane label="已进行的会议" name="done">
                    <el-table :data="DoneList" border stripe>
                        <el-table-column type="index"></el-table-column>
                        <el-table-column label="申请部门" prop="deptName" width="85"></el-table-column>
                        <el-table-column label="会议室" prop="roomNo"></el-table-column>
                        <el-table-column label="参会人数" prop="meetingSize"></el-table-column>
                        <el-table-column label="会议主题" prop="meetingTheme"></el-table-column>
                        <el-table-column label="开会日期">
                            <template slot-scope="scope">
                                <div>
                                    {{ $moment(scope.row.meetingDate).format('YYYY/MM/DD') }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="开会时间">
                            <template slot-scope="scope">
                                <div v-if="scope.row.meetingSlot == 1">9:00-10:30</div>
                                <div v-if="scope.row.meetingSlot == 2">10:30-12:00</div>
                                <div v-if="scope.row.meetingSlot == 3">13:00-15:00</div>
                                <div v-if="scope.row.meetingSlot == 4">15:00-17:00</div>
                                <div v-if="scope.row.meetingSlot == 5">17:00-19:00</div>
                            </template>
                        </el-table-column>
                    </el-table>

                    <!-- 分页区域 -->
                    <el-pagination
                    @size-change="handleSizeChangeDone"
                    @current-change="handleCurrentChangeDone"
                    :current-page="queryDone.currentPage"
                    :page-sizes="[2, 4, 8, 10]"
                    :page-size="queryDone.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="doneTotal">
                    </el-pagination>
                </el-tab-pane>
                <el-tab-pane label="未进行的会议" name="notdone">
                    <el-table :data="NotDoneList" border stripe>
                        <el-table-column type="index"></el-table-column>
                        <el-table-column label="申请部门" prop="deptName" width="85"></el-table-column>
                        <el-table-column label="会议室" prop="roomNo"></el-table-column>
                        <el-table-column label="参会人数" prop="meetingSize"></el-table-column>
                        <el-table-column label="会议主题" prop="meetingTheme"></el-table-column>
                        <el-table-column label="开会日期">
                            <template slot-scope="scope">
                                <div>
                                    {{ $moment(scope.row.meetingDate).format('YYYY/MM/DD') }}
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="开会时间">
                            <template slot-scope="scope">
                                <div v-if="scope.row.meetingSlot == 1">9:00-10:30</div>
                                <div v-if="scope.row.meetingSlot == 2">10:30-12:00</div>
                                <div v-if="scope.row.meetingSlot == 3">13:00-15:00</div>
                                <div v-if="scope.row.meetingSlot == 4">15:00-17:00</div>
                                <div v-if="scope.row.meetingSlot == 5">17:00-19:00</div>
                            </template>
                        </el-table-column>
                    </el-table>

                    <!-- 分页区域 -->
                    <el-pagination
                    @size-change="handleSizeChangeNotDone"
                    @current-change="handleCurrentChangeNotDone"
                    :current-page="queryNotDone.currentPage"
                    :page-sizes="[2, 4, 8, 10]"
                    :page-size="queryNotDone.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="notDoneTotal">
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
            DoneList: [],
            NotDoneList: [],
            activeName: 'done',
            queryDone: {
                pageSize: 4,
                currentPage: 1,
                pass: 1
            },
            queryNotDone: {
                pageSize: 4,
                currentPage: 1,
                pass: 0
            },
            doneTotal: 0,
            notDoneTotal: 0
        }
    },
    created () {
        this.getDoneList()
        this.getNotDoneList()
    },
    methods: {
        async getDoneList () {
           const { data: res } = await this.$http.get('/meeting/history', { params: this.queryDone })
           if (res.code !== 100) {
               return this.$message.error('获取历史会议列表失败！')
            }
            this.DoneList = res.extend.result.list
            this.doneTotal = res.extend.result.total
           console.log(res)
        },
        async getNotDoneList () {
            const { data: res } = await this.$http.get('/meeting/history', { params: this.queryNotDone })
            if (res.code !== 100) {
               return this.$message.error('获取历史会议列表失败！')
            }
            this.NotDoneList = res.extend.result.list
            this.notDoneTotal = res.extend.result.total
        },
        handleSizeChangeDone (newSize) {
            this.queryDone.pageSize = newSize
            this.getDoneList()
        },
        handleCurrentChangeDone (newPage) {
            this.queryDone.currentPage = newPage
            this.getDoneList()
        },
        handleSizeChangeNotDone (newSize) {
            this.queryNotDone.pageSize = newSize
            this.getNotDoneList()
        },
        handleCurrentChangeNotDone (newPage) {
            this.queryNotDone.currentPage = newPage
            this.getNotDoneList()
        }

    }
}
</script>

<style scoped>
    .el-tabs {
        margin-top: 15px;
    }
</style>
