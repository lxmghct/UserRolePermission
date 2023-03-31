<template>
    <div class="main">
        <my-nav></my-nav>
        <h1>Page2</h1>
    </div>
</template>

<script>
import MyNav from '@/components/MyNav.vue'
export default {
  components: { MyNav },
}
</script>

<style scoped>
.main {
    margin: 50px auto;
    text-align: center;
}
</style>
