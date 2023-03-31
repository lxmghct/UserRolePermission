<template>
    <div class="main">
        <my-nav></my-nav>
        <h1>Home</h1>
    </div>
</template>

<script>
import MyNav from '@/components/MyNav.vue'
export default {
    name: 'Home',
    components: {
        MyNav
    }
}
</script>

<style scoped>
.main {
    margin: 50px auto;
    text-align: center;
}
</style>
