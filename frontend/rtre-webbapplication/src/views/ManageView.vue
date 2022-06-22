<template>
<v-app>
    <div v-if="this.adminTrue===true">
        <ManageHub />
        <SearchBox />
    </div>
    <div v-if="this.adminTrue===false">
        <h1>Current User is not an Administrator</h1></div>
</v-app>
</template>

<script>
import axios from "axios";
import SearchBox from "../components/SearchBox.vue";
import ManageHub from "../components/ManageHub.vue"
export default {
    name: 'ManageView',
    components: {
        SearchBox,
        ManageHub,
    },
    data() {
        return {
            adminTrue: false,
            projects: []
        }
    },
    mounted(){
        let UserType = sessionStorage.getItem('UserType');
        if(UserType == 'ADMIN'){ this.adminTrue = true; }
        
        axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getProjectList?token=" + sessionStorage.getItem('TokenId')).then((resp) => {
            this.projects = resp.data;
        });

    }
}
</script>
