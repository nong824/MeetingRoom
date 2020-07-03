<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img src="../assets/meeting.png" alt />
        <span>会议室后台管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边栏菜单区域 -->
        <el-menu :default-active="activePath" :router="true" :collapse-transition="false" :collapse="isCollapse" :unique-opened="true" background-color="#333744" text-color="#fff" active-text-color="#409eff">
          <!-- 一级菜单 -->
          <el-submenu :index="item.id + ''" v-for="item in menulist" :key="item.id">
            <!-- 一级菜单的模板 -->
            <template slot="title">
              <!-- 图标 -->
              <i :class="iconsObj[item.id]"></i>
              <!-- 文本 -->
              <span>{{ item.authName }}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item @click="saveNavState('/' + subItem.path)" :index="'/' + subItem.path" v-for="subItem in item.children" :key="subItem.id">
                <template slot="title">
                    <!-- 图标 -->
                    <i :class="iconsObj[subItem.id]"></i>
                    <!-- 文本 -->
                    <span>{{ subItem.authName }}</span>
                </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧内容 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
    data () {
        return {
            // 左侧菜单数据
            menulist: [{
                id: 101,
                authName: '会议室管理',
                path: 'null',
                children: [
                    { id: 1011, authName: '会议室信息', path: 'meetingRooms' },
                    { id: 1012, authName: '会议室历史会议', path: 'meetingRoomHistory' }
                ]
                },
                {
                id: 102,
                authName: '部门管理',
                path: 'null',
                children: [
                    { id: 1021, authName: '部门信息', path: 'department' },
                    { id: 1022, authName: '部门历史会议', path: 'depHistory' }
                ]
                },
                {
                id: 103,
                authName: '审批申请',
                path: 'null',
                children: [
                    { id: 1031, authName: '已审批', path: 'approved' },
                    { id: 1032, authName: '未审批', path: 'notApproved' }
                ]
                }],
            iconsObj: {
                101: 'iconfont icon-baobiao',
                102: 'iconfont icon-users',
                103: 'iconfont icon-3702mima',
                1011: 'el-icon-info',
                1012: 'el-icon-date',
                1021: 'el-icon-info',
                1022: 'el-icon-date',
                1032: 'el-icon-folder-opened',
                1031: 'el-icon-folder-checked'
            },
            isCollapse: false,
            activePath: ''
        }
    },
    created () {
        this.activePath = window.sessionStorage.getItem('activePath')
    },
    methods: {
        logout () {
            window.sessionStorage.clear()
            this.$router.push('/login')
        },
        // 点击按钮,切换菜单的折叠与展开
        toggleCollapse () {
            this.isCollapse = !this.isCollapse
        },
        // 保存链接的激活状态
        saveNavState (activePath) {
          window.sessionStorage.setItem('activePath', activePath)
          this.activePath = activePath
        }
    }
}
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    height: 100%;
    img {
      height: 100%;
    }
    span {
      margin-left: 15px;
    }
  }
}

.el-menu {
    border-right: 0;
}

.el-aside {
  background-color: #333744;

  .toggle-button {
      background-color: #4a5064;
      cursor: pointer;
      color: #fff;
      font-size: 10px;
      line-height: 24px;
      text-align: center;
      letter-spacing: .2em;
  }

  .el-main {
      border-right: 0;
  }
}

.iconfont {
    margin-right: 10px;
}
</style>
