<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>会议室管理</el-breadcrumb-item>
            <el-breadcrumb-item>会议室信息</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图区 -->
        <el-card>
            <el-row :gutter="20">
                <el-col :span="7">
                    <el-input placeholder="请输入会议室号" v-model="keyWord" clearable @clear="getRoomList">
                        <el-button slot="append" icon="el-icon-search" @click="selectRoom"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">添加会议室</el-button>
                </el-col>
            </el-row>

            <!-- 会议室列表区 -->
            <el-table :data="roomList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="会议室号" prop="roomNo"></el-table-column>
                <el-table-column label="可容纳人数" prop="roomSize"></el-table-column>
                <el-table-column label="会议室状态" prop="roomstatus">
                    <template slot-scope="scope">
                        <el-switch @change="roomStatusChanged(scope.row)" v-model="scope.row.roomStatus"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="空调状态" prop="roomstatus">
                    <template slot-scope="scope">
                        <el-switch @change="airStatusChanged(scope.row)" v-model="scope.row.air"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="投影仪状态" prop="roomstatus">
                    <template slot-scope="scope">
                        <el-switch @change="projectorStatusChanged(scope.row)" v-model="scope.row.projector"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180px">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-tooltip class="item" effect="dark" content="修改会议室信息" placement="top" :enterable="false">
                            <el-button size="mini" type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.roomId)"></el-button>
                        </el-tooltip>
                        <!-- 删除按钮 -->
                        <el-tooltip class="item" effect="dark" content="删除会议室" placement="top" :enterable="false">
                            <el-button size="mini" type="danger" icon="el-icon-delete" @click="removeRoomById(scope.row.roomId)"></el-button>
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
            <el-form-item label="会议室号" prop="roomNo">
                <el-input v-model="addForm.roomNo"></el-input>
            </el-form-item>
            <el-form-item label="可容纳人数" prop="roomSize">
                <el-input type="number" v-model="addForm.roomSize"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="addDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addRoom">确 定</el-button>
        </span>
        </el-dialog>

        <!-- 修改会议室信息对话框 -->
        <el-dialog
        title="修改会议室信息"
        :visible.sync="editDialogVisible"
        width="50%"
        @close="editDialogClosed">
        <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
            <el-form-item label="会议室号" prop="roomNo">
                <el-input v-model="editForm.roomNo"></el-input>
            </el-form-item>
            <el-form-item label="可容纳人数" prop="roomSize">
                <el-input type="number" v-model="editForm.roomSize"></el-input>
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
        return {
            queryInfo: {
                currentPage: 1,
                pageSize: 5
            },
            roomList: [],
            total: 0,
            keyWord: '',
            addDialogVisible: false,
            editDialogVisible: false,
            addForm: {
                roomNo: '',
                roomSize: 0
            },
            addFormRules: {
                roomNo: [
                    { required: true, message: '请输入会议室号', trigger: 'blur' },
                    { min: 3, max: 4, message: '会议室号长度在3~4个数字之间', trigger: 'blur' }
                ],
                roomSize: [
                    { required: true, message: '请输入可容纳人数', trigger: 'blur' }
                ]
            },
            editForm: {},
            editFormRules: {
                roomNo: [
                    { required: true, message: '请输入会议室号', trigger: 'blur' },
                    { min: 3, max: 4, message: '会议室号长度在3~4个数字之间', trigger: 'blur' }
                ],
                roomSize: [
                    { required: true, message: '请输入可容纳人数', trigger: 'blur' }
                ]
            }
        }
    },
    created () {
        this.getRoomList()
    },
    methods: {
        async getRoomList () {
            const { data: res } = await this.$http.get('/rooms', { params: this.queryInfo })
            if (res.code !== 100) {
                return this.$message.error('获取会议室信息失败！')
            }
            this.roomList = res.extend.result.list
            this.total = res.extend.result.total
        },
        // 监听pageSize改变的事情
        handleSizeChange (newSize) {
            this.queryInfo.pageSize = newSize
            this.getRoomList()
        },
        // 监听页码值改变的事件
        handleCurrentChange (newPage) {
            this.queryInfo.currentPage = newPage
            this.getRoomList()
        },
        // 监听switch状态
        async roomStatusChanged (roomInfo) {
            const { data: res } = await this.$http.put('/room', { roomId: roomInfo.roomId, roomStatus: roomInfo.roomStatus })
            if (res.code !== 100) {
                roomInfo.roomStatus = !roomInfo.roomStatus
                return this.$message.error('修改会议室状态失败！')
            } else {
                this.$message.success('修改会议室状态成功！')
            }
        },
        async airStatusChanged (roomInfo) {
            const { data: res } = await this.$http.put('/room', { roomId: roomInfo.roomId, air: roomInfo.air })
            if (res.code !== 100) {
                roomInfo.roomStatus = !roomInfo.roomStatus
                return this.$message.error('修改空调状态失败！')
            } else {
                this.$message.success('修改空调状态成功！')
            }
        },
        async projectorStatusChanged (roomInfo) {
            const { data: res } = await this.$http.put('/room', { roomId: roomInfo.roomId, projector: roomInfo.projector })
            if (res.code !== 100) {
                roomInfo.roomStatus = !roomInfo.roomStatus
                return this.$message.error('修改投影仪状态失败！')
            } else {
                this.$message.success('修改投影仪状态成功！')
            }
        },
        async selectRoom () {
            const { data: res } = await this.$http.get('/roomNo', { params: { roomNo: this.keyWord, currentPage: this.queryInfo.currentPage, pageSize: this.queryInfo.pageSize } })
            if (res.code !== 100) {
                return this.$message.error('查询会议室信息失败！')
            }
            this.roomList = res.extend.result.list
            this.total = res.extend.result.total
            this.$message.success('查询会议室信息成功！')
        },
        addDialogClosed () {
            this.$refs.addFormRef.resetFields()
        },
        editDialogClosed () {
            this.$refs.editFormRef.resetFields()
        },
        addRoom () {
            this.$refs.addFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/room', { roomNo: this.addForm.roomNo, roomSize: this.addForm.roomSize - 0 })
                if (res.code !== 100) {
                    return this.$message.error('添加会议室失败！')
                }
                this.$message.success('添加会议室成功！')
                this.addDialogVisible = false
                this.getRoomList()
            })
        },
        async showEditDialog (id) {
            const { data: res } = await this.$http.get('/room', { params: { roomId: id } })
            console.log(res)
            if (res.code !== 100) {
                return this.$message.error('查询会议室信息失败！')
            }
            this.editForm = res.extend.result
            this.editDialogVisible = true
        },
        eidtRoom () {
            this.$refs.editFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.put('/room', { roomId: this.editForm.roomId, roomNo: this.editForm.roomNo, roomSize: this.editForm.roomSize - 0 })
                if (res.code !== 100) {
                    return this.$message.error('修改会议室信息失败！')
                } else {
                    this.editDialogVisible = false
                    this.getRoomList()
                    this.$message.success('修改会议室信息成功！')
                }
            })
        },
        async removeRoomById (id) {
            const confirmResult = await this.$confirm('此操作将永久删除该会议室, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (confirmResult !== 'confirm') {
                return this.$message.info('已取消删除')
            }
            const { data: res } = await this.$http.delete('/room', { data: { roomId: id } })
            if (res.code !== 100) {
                return this.$message.error('删除会议室失败！')
            }
            this.$message.success('删除会议室成功')
            this.getRoomList()
        }
    }
}
</script>

<style lang="less" scoped>

</style>
