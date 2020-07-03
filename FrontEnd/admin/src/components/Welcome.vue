<template>
    <div>
        <el-card>
            欢迎使用
        </el-card>
    </div>
</template>

<script>
// import moment from 'moment'
// export default {
//     created () {
//         this.getDateLst()
//         this.queryInfo.applyDate = this.dateLst[0].date
//         this.getRoomList()
//     },
//     methods: {
//         getDateLst () {
//             for (let i = 0; i < 7; i++) {
//                 let active = false
//                 const date = moment().add(i, 'days').format('YYYY-MM-DD')
//                 const weekLst = ['日', '一', '二', '三', '四', '五', '六']
//                 const dateLocal = moment().add(i, 'days')
//                 const week = '星期' + weekLst[dateLocal._d.getDay()]
//                 if (i === 0) active = true
//                 this.dateLst.push({ week, date, active })
//             }
//         },
//         async getRoomList () {
//             const { data: res } = await this.$http.get('/apply/free', { params: this.queryInfo })
//             if (res.code !== 100) {
//                 return this.$message.error('获取会议室使用情况失败！')
//             } else {
//                 this.roomList = res.extend.result
//                 // console.log(this.roomList)
//             }
//             var length = this.roomList.length
//             for (let i = 0; i < length; i++) {
//                 this.roomList[i].count = 0
//                 for (var o in this.roomList[i]) {
//                     if (this.roomList[i][o] === false) {
//                         this.roomList[i].count++
//                     }
//                 }
//             }
//             for (let i = 0; i < length; i++) {
//                 const { roomNo: name, count: value } = this.roomList[i]
//                 this.xData.push(name)
//                 this.yData.push(value)
//             }
//         }
//     },
//     data () {
//         return {
//             dateLst: [],
//             queryInfo: {
//                 applyDate: ''
//             },
//             roomList: [],
//             xData: [],
//             yData: []
//         }
//     }
// }
</script>
