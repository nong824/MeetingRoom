<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>审批申请</el-breadcrumb-item>
            <el-breadcrumb-item>未审批</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card>
            <el-table :data="applcationList" border stripe>
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
                <el-table-column label="申请时间">
                    <template slot-scope="scope">
                        <div>
                            {{ $moment(scope.row.applyTime).format('YYYY/MM/DD') }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <!-- 同意按钮 -->
                        <el-tooltip class="item" effect="dark" content="同意申请" placement="top" :enterable="false">
                            <el-button type="success" icon="el-icon-check" circle @click="accept(scope.row.applicationId)"></el-button>
                        </el-tooltip>
                        <!-- 拒绝按钮 -->
                        <el-tooltip class="item" effect="dark" content="拒绝申请" placement="top" :enterable="false">
                            <el-button type="danger" icon="el-icon-delete" circle @click="reject(scope.row.applicationId)"></el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页区域 -->
            <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.currentPage"
            :page-sizes="[5, 10]"
            :page-size="queryInfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </el-card>

        <!-- 拒绝申请对话框 -->
        <el-dialog
        title="请填写拒绝理由"
        :visible.sync="rejDialogVisible"
        width="50%"
        @close="rejDialogClosed">
        <el-form :model="rejForm" :rules="rejFormRules" ref="rejFormRef" label-width="100px">
            <el-form-item label="拒绝理由" prop="rejectReason">
                <el-input type="textarea" v-model="rejForm.rejectReason" placeholder="请输入拒绝理由"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="rejDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="rej()">确 定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data () {
        return {
            rejDialogVisible: false,
            applcationList: [],
            queryInfo: {
                pageSize: 5,
                currentPage: 1,
                auditStatus: 0
            },
            total: 0,
            rejForm: {
                applicationId: 0,
                rejectReason: ''
            },
            rejFormRules: {
                rejectReason: [
                    { required: true, message: '请输入拒绝理由', trigger: 'blur' },
                    { max: 15, message: '拒绝理由不超过15个字符！', trigger: 'blur' }
                ]
            }
        }
    },
    created () {
        this.getList()
    },
    methods: {
        async getList () {
            const { data: res } = await this.$http.get('/apply/history', { params: this.queryInfo })
            if (res.code !== 100) {
                return this.$message.error('获取审核列表失败！')
            } else {
                this.total = res.extend.result.total
                this.applcationList = res.extend.result.list
            }
        },
        async accept (id) {
            const confirmResult = await this.$confirm('此操作将批准这次申请, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (confirmResult !== 'confirm') {
                return this.$message.info('已取消批准操作')
            }
            const { data: res } = await this.$http.put('/apply/accept', { applicationId: id })
            if (res.code !== 100) {
                return this.$message.error('批准申请失败！')
            } else {
                this.$message.success('批准申请成功！')
                this.getList()
            }
        },
        reject (id) {
            this.rejForm.applicationId = id
            this.rejDialogVisible = true
        },
        rejDialogClosed () {
            this.$refs.rejFormRef.resetFields()
        },
        async rej () {
            const confirmResult = await this.$confirm('此操作将拒绝这次申请, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (confirmResult !== 'confirm') {
                this.rejDialogVisible = false
                return this.$message.info('已取消拒绝操作')
            }
            this.$refs.rejFormRef.validate(async valid => {
                if (!valid) return
                // console.log(this.rejForm)
                const { data: res } = await this.$http.put('/apply/reject', this.rejForm)
                // console.log(res)
                if (res.code !== 100) {
                    return this.$message.error('拒绝申请失败！')
                } else {
                    this.$message.success('拒绝申请成功！')
                    this.rejDialogVisible = false
                    this.getList()
                }
            })
        },
        handleSizeChange (newSize) {
            this.queryInfo.pageSize = newSize
            this.getList()
        },
        handleCurrentChange (newPage) {
            this.queryInfo.currentPage = newPage
            this.getList()
        }
    }
}
</script>
