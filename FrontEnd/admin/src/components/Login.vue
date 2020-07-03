<template>
    <div class="login_container">
        <div class="login_box">
            <!-- 头像区域 -->
            <div class="avatar_box">
                <img src="../assets/admin.png" alt="">
            </div>

            <!-- 登录表单区域 -->
            <el-form ref="loginFormRef" class="login_form" :rules="loginFormRules" :model="loginForm">
                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" prefix-icon="iconfont icon-user" placeholder="请输入登录名称"></el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" prefix-icon="iconfont icon-3702mima" type="password" placeholder="请输入登录密码"></el-input>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item class="btns">
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-button type="info" @click="resetLoginForm">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    data () {
        return {
            // 登陆表单的数据绑定对象
            loginForm: {
                username: 'admin',
                password: 'admin'
            },
            // 这是表单的验证规则对象
            loginFormRules: {
                // 验证用户名是否合法
                username: [
                    { required: true, message: '请输入登录名称', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                // 验证密码是否合法
                password: [
                    { required: true, message: '请输入登录密码', trigger: 'blur' },
                    { min: 5, max: 10, message: '密码长度在 5 到 10 个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        // 点击重置按钮，重置登录表单
        resetLoginForm () {
            this.$refs.loginFormRef.resetFields()
        },
        login () {
            this.$refs.loginFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/manager', this.loginForm)
                if (res.code !== 100) return this.$message.error('登陆失败，用户名或者密码错误！')
                this.$message.success('登陆成功')
                // 1.将登陆成功之后的token，保存到客户端中的sessionStorage中
                //   1.1 项目中除了登录之外的其他API接口，必须在登录之后才能访问
                //   1.2 token只在当前网站打开期间生效，所以将token保存在sessionStorage中
                window.sessionStorage.setItem('token', res.extend.token)
                // 2.通过编程式导航跳转到后台主页，路由地址是/home
                this.$router.push('/home')
            })
        }
    }
}
</script>

<style lang="less" scoped>
.login_container{
    background-color: #2b4b6b;
    height: 100%;
}

.login_box{
    width: 450px;
    height: 300px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);

    .avatar_box{
        height: 130px;
        width: 130px;
        background-color: #fff;
        border: 1px solid #eee;
        border-radius: 50%;
        padding: 10px;
        position: absolute;
        transform: translate(-50%, -50%);
        left: 50%;
        img{
            width: 100%;
            height: 100%;
            background-color: #eee;
            border-radius: 50%;
        }
    }
}

.login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}

.btns{
    display: flex;
    justify-content: flex-end;
}
</style>
