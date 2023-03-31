<template>
    <div>
        <div class="btn"
             @click="routeTo('/home')"
             v-if="$permission.has('TEST_MAIN')">home</div>
        <div class="btn"
             @click="routeTo('/page1')"
             v-if="$permission.has('TEST_PAGE1')">page1</div>
        <div class="btn"
             @click="routeTo('/page2')"
             v-if="$permission.has('TEST_MAIN')">page2</div>
    </div>
</template>

<script>
export default {
    name: 'MyNav',
    methods: {
        routeTo(route) {
            this.$router.push(route)
        }
    }
}
</script>

<style scoped>
.btn {
    width: 100px;
    height: 40px;
    background-color: rgb(0, 182, 227);
    cursor: pointer;
    text-align: center;
    line-height: 40px;
    font-size: 20px;
    margin: 10px;
    border-radius: 10px;
    display: inline-block;
}
.btn:hover {
    background-color: rgb(26, 203, 248);
}

</style>