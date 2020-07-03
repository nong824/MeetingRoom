<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>部门管理</el-breadcrumb-item>
            <el-breadcrumb-item>部门信息</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图区 -->
        <el-card>
            <el-row :gutter="20">
                <el-col :span="7">
                    <el-input placeholder="请输部门名" v-model="keyWord" clearable @clear="getDeptList">
                        <el-button slot="append" icon="el-icon-search" @click="selectDept"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">添加部门</el-button>
                </el-col>
            </el-row>

            <!-- 会议室列表区 -->
            <el-table :data="deptList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="部门号" prop="deptNo"></el-table-column>
                <el-table-column label="部门名" prop="deptName"></el-table-column>
                <el-table-column label="部门手机" prop="deptPhone"></el-table-column>
                <el-table-column label="部门密码" prop="deptPassword"></el-table-column>
                <el-table-column label="操作" width="180px">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-tooltip class="item" effect="dark" content="修改部门信息" placement="top" :enterable="false">
                            <el-button size="mini" type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.deptId)"></el-button>
                        </el-tooltip>
                        <!-- 删除按钮 -->
                        <el-tooltip class="item" effect="dark" content="删除部门" placement="top" :enterable="false">
                            <el-button size="mini" type="danger" icon="el-icon-delete" @click="removeDeptById(scope.row.deptId)"></el-button>
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

        <!-- 添加会议室对话框 -->
        <el-dialog
        title="添加会议室"
        :visible.sync="addDialogVisible"
        width="50%"
        @close="addDialogClosed">
        <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
            <el-form-item label="部门号" prop="deptNo">
                <el-input v-model="addForm.deptNo"></el-input>
            </el-form-item>
            <el-form-item label="部门名" prop="deptName">
                <el-input v-model="addForm.deptName"></el-input>
            </el-form-item>
            <el-form-item label="部门手机" prop="deptPhone">
                <el-input v-model="addForm.deptPhone"></el-input>
            </el-form-item>
            <el-form-item label="部门密码" prop="deptPassword">
                <el-input v-model="addForm.deptPassword"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="addDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addDept">确 定</el-button>
        </span>
        </el-dialog>

        <!-- 修改会议室信息对话框 -->
        <el-dialog
        title="修改会议室信息"
        :visible.sync="editDialogVisible"
        width="50%"
        @close="editDialogClosed">
        <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
            <el-form-item label="部门号" prop="deptNo">
                <el-input v-model="editForm.deptNo"></el-input>
            </el-form-item>
            <el-form-item label="部门名" prop="deptName">
                <el-input v-model="editForm.deptName"></el-input>
            </el-form-item>
            <el-form-item label="部门手机" prop="deptPhone">
                <el-input v-model="editForm.deptPhone"></el-input>
            </el-form-item>
            <el-form-item label="部门密码" prop="deptPassword">
                <el-input v-model="editForm.deptPassword"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="eidtRoom">确 定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data () {
        // 验证手机号
        var checkMobile = (rule, value, cb) => {
            const regMobile = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/
            if (regMobile.test(value)) {
                return cb()
            }
            cb(new Error('请输入格式正确的手机'))
        }
        return {
            queryInfo: {
                currentPage: 1,
                pageSize: 5
            },
            deptList: [],
            total: 0,
            keyWord: '',
            addDialogVisible: false,
            editDialogVisible: false,
            addForm: {
                deptNo: '',
                deptName: '',
                deptPhone: '',
                deptPassword: ''
            },
            addFormRules: {
                deptNo: [
                    { required: true, message: '请输入部门号', trigger: 'blur' },
                    { min: 4, max: 4, message: '会议室号长度在3~4个数字之间', trigger: 'blur' }
                ],
                deptName: [
                    { required: true, message: '请输入部门名', trigger: 'blur' }
                ],
                deptPhone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { validator: checkMobile, trigger: 'blur' }
                ],
                deptPassword: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ]
            },
            editForm: {},
            editFormRules: {
                deptNo: [
                    { required: true, message: '请输入部门号', trigger: 'blur' },
                    { min: 4, max: 4, message: '会议室号长度在3~4个数字之间', trigger: 'blur' }
                ],
                deptName: [
                    { required: true, message: '请输入部门名', trigger: 'blur' }
                ],
                deptPhone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { validator: checkMobile, trigger: 'blur' }
                ],
                deptPassword: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ]
            }
        }
    },
    created () {
        this.getDeptList()
    },
    methods: {
        async getDeptList () {
            const { data: res } = await this.$http.get('/depts', { params: this.queryInfo })
            if (res.code !== 100) {
                return this.$message.error('获取会议室信息失败！')
            }
            this.deptList = res.extend.result.list
            this.total = res.extend.result.total
        },
        // 监听pageSize改变的事情
        handleSizeChange (newSize) {
            this.queryInfo.pageSize = newSize
            this.getDeptList()
        },
        // 监听页码值改变的事件
        handleCurrentChange (newPage) {
            this.queryInfo.currentPage = newPage
            this.getDeptList()
        },
        async selectDept () {
            const { data: res } = await this.$http.get('/deptCondition', { params: { deptName: this.keyWord, currentPage: this.queryInfo.currentPage, pageSize: this.queryInfo.pageSize } })
            if (res.code !== 100) {
                return this.$message.error('查询部门信息失败！')
            }
            console.log(res)
            this.deptList = res.extend.result.list
            this.total = res.extend.result.total
            this.$message.success('查询会议室信息成功！')
        },
        addDialogClosed () {
            this.$refs.addFormRef.resetFields()
        },
        editDialogClosed () {
            this.$refs.editFormRef.resetFields()
        },
        addDept () {
            this.$refs.addFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/dept', this.addForm)
                if (res.code !== 100) {
                    return this.$message.error('添加部门失败！')
                }
                this.$message.success('添加部门成功！')
                this.addDialogVisible = false
                this.getDeptList()
            })
        },
        async showEditDialog (id) {
            const { data: res } = await this.$http.get('/dept', { params: { deptId: id } })
            if (res.code !== 100) {
                return this.$message.error('查询部门信息失败！')
            }
            this.editForm = res.extend.result
            this.editDialogVisible = true
        },
        eidtRoom () {
            this.$refs.editFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.put('/dept', this.editForm)
                if (res.code !== 100) {
                    return this.$message.error('修改部门信息失败！')
                } else {
                    this.editDialogVisible = false
                    this.getDeptList()
                    this.$message.success('修改部门信息成功！')
                }
            })
        },
        async removeDeptById (id) {
            const confirmResult = await this.$confirm('此操作将永久删除该部门, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (confirmResult !== 'confirm') {
                return this.$message.info('已取消删除')
            }
            const { data: res } = await this.$http.delete('/dept', { data: { deptId: id } })
            if (res.code !== 100) {
                return this.$message.error('删除部门失败！')
            }
            this.$message.success('删除部门成功')
            this.getDeptList()
        }
    }
}
</script>

<style lang="less" scoped>

</style>
