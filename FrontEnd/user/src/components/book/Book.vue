<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>申请会议室</el-breadcrumb-item>
            <el-breadcrumb-item>经行预约</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图 -->
        <el-card>
            <div class="resRoomPluginBox">
                <div class="resHead">
                    <div class="identifyLst">
                        <li class="identify" v-for="(item, index) in identifyLst" :key="index">
                            <i :class="{ 'nooccupy':index == 1, 'select':index == 2 }"></i><span>{{ item }}</span>
                        </li>
                    </div>
                </div>
                <div class="roomRerservation">
                    <div class="roomTop">
                        <div class="roomTopSide">
                            <span class="titleTime">时间</span>
                            <span class="titleName">会议室</span>
                        </div>
                        <div class="roomTopTime">
                            <ul class="roomDate">
                                <li class="dateItem" v-for="(date, key) in dateLst" :class="{'active':date.active}" @click="changeDate(key)" :key="key"><span>{{date.week}}<br/>{{date.date}}</span></li>
                            </ul>
                            <ul class="roomTopHour">
                                <li class="hourNavItem" v-for="(item, index) in hourList" :key="index">{{item}}</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="roomLine" v-for="item in roomList" :key="item.roomId">
                    <div class="roomName">
                        <span>{{item.roomNo+'('+item.roomSize+'人)'}}</span>
                    </div>
                    <div class="roomStatus">
                        <ul class="roomGrid">
                            <li class="roomItem" v-for="(room, index) in [item.one, item.two, item.three, item.four, item.five]"
                            :key="index"
                            :class="{'isOccupy':room == false, 'isSelected':item.roomId == selectedInfo.roomId && index == selectedInfo.applySlot-1}"
                            @click="selectRoom(item.roomNo, item.roomId, index, room, item.roomSize, item.air, item.projector)"></li>
                        </ul>
                    </div>
                </div>
            </div>

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

            <el-form ref="form" :rules="selectRule" :model="selectedInfo" label-width="100px">
                <el-form-item label="会议室：" size="mini">
                    <el-input v-model="selectedInfo.roomNo" disabled></el-input>
                </el-form-item>
                <el-form-item label="会议室设备：" size="mini">
                    <div class="tag">
                        <el-tag type="success" v-if="selectedInfo.air === true">空调可用</el-tag>
                        <el-tag type="danger" v-else-if="selectedInfo.air === false">空调不可用</el-tag>
                        <el-tag type="info" v-else>空调状态</el-tag>
                    </div>
                    <div class="tag">
                        <el-tag type="success" v-if="selectedInfo.projector === true">投影仪可用</el-tag>
                        <el-tag type="danger" v-else-if="selectedInfo.projector === false">投影仪不可用</el-tag>
                        <el-tag type="info" v-else>投影仪状态</el-tag>
                    </div>
                </el-form-item>
                <el-form-item label="开会日期：" size="mini">
                    <el-input v-model="selectedInfo.applyDate" disabled></el-input>
                </el-form-item>
                <el-form-item label="开会时间：" size="mini">
                    <el-input v-model="selectedInfo.displaySlot" disabled></el-input>
                </el-form-item>
                <el-form-item label="会议主题：" prop="meetingTheme">
                    <el-input type="textarea" placeholder="请输入会议主题，不超过15个字符。" v-model="selectedInfo.meetingTheme"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submit()">提交申请</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import moment from 'moment'
export default {
    data () {
        return {
            selectedInfo: {
                applyDate: '',
                meetingTheme: '',
                roomNo: '',
                applySlot: '',
                roomId: '',
                displaySlot: '',
                roomSize: '',
                air: '',
                projector: ''
            },
            queryInfo: {
                pageSize: 5,
                currentPage: 1,
                applyDate: ''
            },
            total: 0,
            selectedId: '',
            selectedSlot: '',
            identifyLst: ['被占用', '空闲', '选择中'],
            dateLst: [],
            hourList: ['9:00-10:30', '10:30-12:00', '13:00-15:00', '15:00-17:00', '17:00-19:00'],
            selectRule: {
                meetingTheme: [
                    { required: true, message: '请输入会议主题', trigger: 'blur' },
                    { max: 15, message: '会议主题不超过15个字符！', trigger: 'blur' }
                ]
            },
            roomList: []
        }
    },
    methods: {
        async getRoomList () {
            const { data: res } = await this.$http.get('/apply/free', { params: this.queryInfo })
            if (res.code !== 100) {
                return this.$message.error('获取会议室使用情况失败！')
            } else {
                this.total = res.extend.result.total
                this.roomList = res.extend.result.list
            }
        },
        getDateLst () {
            for (let i = 0; i < 7; i++) {
                let active = false
                const date = moment().add(i, 'days').format('YYYY-MM-DD')
                const weekLst = ['日', '一', '二', '三', '四', '五', '六']
                const dateLocal = moment().add(i, 'days')
                const week = '星期' + weekLst[dateLocal._d.getDay()]
                if (i === 0) active = true
                this.dateLst.push({ week, date, active })
            }
        },
        changeDate (key) {
            this.dateLst.forEach((item, key) => {
                item.active = false
            })
            this.dateLst[key].active = true
            this.selectedInfo.applyDate = this.dateLst[key].date
            this.queryInfo.applyDate = this.dateLst[key].date
            this.getRoomList()
            // this.hideMarkLine()
        },
        selectRoom (roomNo, roomId, timeSlot, status, roomSize, air, projector) {
            if (status === false) {
                return this.$message.warning('选中的时间段已经被占用，请重新选择！')
            } else {
                this.selectedInfo.roomId = roomId
                this.selectedInfo.applySlot = timeSlot + 1
                this.selectedInfo.roomNo = roomNo
                this.selectedInfo.displaySlot = this.hourList[timeSlot]
                this.selectedInfo.roomSize = roomSize
                this.selectedInfo.air = air
                this.selectedInfo.projector = projector
            }
        },
        handleSizeChange (newSize) {
            this.queryInfo.pageSize = newSize
            this.getRoomList()
        },
        handleCurrentChange (newPage) {
            this.queryInfo.currentPage = newPage
            this.getRoomList()
        },
        async submit () {
            this.$refs.form.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/apply', this.selectedInfo)
                if (res.code !== 100) {
                    return this.$message.error('申请失败！')
                } else {
                    this.selectedInfo.meetingTheme = ''
                    this.selectedInfo.roomNo = ''
                    this.selectedInfo.displaySlot = ''
                    this.selectedInfo.roomId = ''
                    this.selectedInfo.applySlot = 0
                    return this.$message.success('申请成功，请等待管理员审核！')
                }
            })
        }
    },
    created () {
        this.getDateLst()
        this.selectedInfo.applyDate = this.dateLst[0].date
        this.queryInfo.applyDate = this.dateLst[0].date
        this.getRoomList()
    }
}
</script>

<style lang="less" scoped>
    @disabledColor: #c8c9cc; //not-allowd颜色
    @hourColor: fadeout(@themeColor, 80%); //时间滚条颜色
    @markLineColor: #f76262; //markline颜色
    @baseColor:#66b1ff;//基础色，比如border
    @themeColor:@baseColor;//主题颜色
    @fontColor:#606266;
    @roomTopSideW: 120px;
    @baseBorder: 1px solid @baseColor;
    @barGridH: 25px;
    @roomTopSideW: 120px;
    @sumW: 900px;
    @topRightW: @sumW - @roomTopSideW;
    @gridW: (@topRightW - @barGridH - @barGridH) / 24;
    @gridH: @gridW;
    @themeColor2:@baseColor;//日期选中颜色
    .tag {
        display: inline-block;
    }

    .el-tag {
        margin: 0 56px;
    }

    .el-form {
        width: 500px;
        margin: 25px auto;
    }

    .resRoomPluginBox {
        width: 900px;
        margin: 0 auto;
    }

    .resHead {
        display: flex;

        .identifyLst {
            display: flex;
            justify-content:flex-end;
            flex: 1;

            .identify {
                padding: 8px 0;
                margin-left: 10px;

                i {
                    display: inline-block;
                    width: 15px;
                    height: 15px;
                    background-color: @disabledColor;
                    position: relative;
                    top: 3px;
                    box-sizing: border-box;
                }

                i.nooccupy {
                    background-color: #fff;
                    border: 1px solid @disabledColor;
                }

                i.select {
                    background-color: @themeColor;
                }

                span {
                    font-size: 12px;
                    margin-left: 2px;
                    color: @fontColor;
                }
            }
        }
    }

    .roomRerservation {
        width: 100%;
        background-color: #fff;
        box-sizing: border-box;
    }

    .roomTop {
        width: 100%;
        height: 70px;
        display: flex;
    }

    .roomTopSide {
        width: @roomTopSideW;
        border: @baseBorder;
        box-sizing: border-box;
        position: relative;
        height: 100%;
        left: -1px;

        &:before {
            content: "";
            position: absolute;
            left: 14px;
            top: -16px;
            width: 100%;
            height: 50px;
            box-sizing: border-box;
            border-bottom: 1.5px solid @baseColor;
            transform-origin: center center;
            transform: rotateZ(30deg) scale(1.16);
        }

        .titleTime,
        .titleName {
            position: absolute;
            font-size: 14px;
            color: @fontColor;
        }

        .titleName {
            bottom: 13px;
            left: 14px;
        }

        .titleTime {
            top: 13px;
            right: 20px;
        }
    }

    .roomTopTime {
        flex: 1;
        height: 100%;
    }

    .roomDate {
        height: calc(100% - @barGridH);
        display: flex;
        background-color: #fff;
        margin-right: -1px;
        margin-left: -1px;

        .dateItem {
            display: flex;
            flex: 1;
            align-items: center;
            justify-content: center;
            font-size: 13px;
            text-align: center;
            box-sizing: border-box;
            border: @baseBorder;
            height: 100%;
            cursor: pointer;
            color: @fontColor;
        }

        .dateItem.active {
            background-color: @themeColor2;
            color: #fff;
        }
    }

    .roomTopHour {
        height: @barGridH;
        display: flex;
        margin-left: -1px;
        margin-right: -1px;

        .hourNavItem {
            flex: 1;
            width: calc(100% / 5);
            height: @barGridH;
            line-height: @barGridH;
            text-align: center;
            box-sizing: border-box;
            border: @baseBorder;
            font-size: 13px;
            background-color: @hourColor;
            color: @fontColor;
        }
    }

    .roomLine {
        width: 100%;
        height: 30px;
        display: flex;
    }

    .roomName {
        width: @roomTopSideW;
        border: @baseBorder;
        box-sizing: border-box;
        position: relative;
        left: -1px;
        line-height: 30px;
    }

    .roomStatus {
        flex: 1;
        height: 100%;
    }

    .roomGrid {
        height: 100%;
        display: flex;
        margin-left: -1px;
        margin-right: -1px;

        .roomItem {
            display: flex;
            flex: 1;
            align-items: center;
            justify-content: center;
            font-size: 13px;
            text-align: center;
            box-sizing: border-box;
            border: @baseBorder;
            height: 100%;
            cursor: pointer;
            color: @fontColor;
        }

        .roomItem.isOccupy {
            background-color: @disabledColor;
            cursor: not-allowed;
        }

        .roomItem.isSelected {
            background-color: @themeColor;
        }
    }
</style>
