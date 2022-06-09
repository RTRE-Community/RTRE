<template>
<v-app  class="ma-10">
    <TopBar></TopBar>
    <router-view></router-view>
</v-app>
</template>

<script>
import TopBar from '@/components/TopBar.vue'
export default {
    name: "App",
    components:{
        TopBar
    }
};
</script>

<style>
.v-application--wrap{
    max-height:100vh
}
</style>