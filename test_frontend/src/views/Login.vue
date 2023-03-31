<template>
    <div class="login-main">
        <h1>Login</h1>
        <form @submit.prevent="login">
            <div>
                <label for="username">Username</label>
                <input type="text" id="username" v-model="username">
            </div>
            <div>
                <label for="password">Password</label>
                <input type="password" id="password" v-model="password">
            </div>
            <div>
                <button type="submit">Login</button>
            </div>
        </form>
    </div>
</template>

<script>
export default {
    data() {
        return {
            username: '',
            password: ''
        }
    },
    methods: {
        login() {
            const xhl = new XMLHttpRequest();
            const url = '/api/user/login'
            const params = 'username=' + this.username + '&password=' + this.password + '&system=测试平台'
            xhl.open('POST', url + '?' + params, true);
            xhl.setRequestHeader('Content-Type', 'application/json');
            // body:
            // xhl.send(JSON.stringify({
            //     username: this.username,
            //     password: this.password,
            //     system: '测试平台'
            // }));
            xhl.send();
            xhl.onreadystatechange = () => {
                if (xhl.readyState === 4) {
                    if (xhl.status === 200) {
                        const res = JSON.parse(xhl.responseText);
                        sessionStorage.setItem('loginInformation', JSON.stringify(res.data));
                        this.$router.push('/home');
                    } else {
                        alert('服务器错误');
                    }
                }
            }
        }
    }
}
</script>

<style scoped>
.login-main {
    width: 400px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}
</style>