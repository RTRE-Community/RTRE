<template>
<div>
    <v-text-field solo label="Search" prepend-inner-icon="mdi-magnify" v-model="search"></v-text-field>

    <ProjectList :projects="filteredProjects"></ProjectList>

</div>
</template>

<script>
import axios from "axios";
import ProjectList from "./ProjectList.vue";
export default {
    name: "SearchBox",
    data() {
        return {
            projects: [],

            search: ""
        };
    },
    mounted() {
        axios.get("http://localhost:3030/api/getProjectList").then((resp) => {
            this.projects = resp.data;
        });
    },
    computed: {
        filteredProjects() {
            console.log(this.projects.filter(project => project.oid.toString().includes(this.search.toString())|project.name.toLowerCase().includes(this.search.toLowerCase())) )
            return this.projects.filter(project => project.oid.toString().includes(this.search.toString()) |project.name.toLowerCase().includes(this.search.toLowerCase()))
        }
    },
    components: {
        ProjectList
    }
}
</script>
