<template>
<div>
    <v-text-field solo label="Search" prepend-inner-icon="mdi-magnify" v-model="search"></v-text-field>

    <div v-if="search">
        <v-expansion-panels v-for="project in filteredProjects" :key="project.id" inset>
            <v-expansion-panel>
                <v-expansion-panel-header color="blue white--text" dark flat>
                    {{project.name}}
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                    <div v-if=" project.parentId != -1" class="py-2">
                        <CheckOutIconButtonVue :oid="projects.oid" :schema="projects.schema" />
                        <DeleteButtonVue :oid="projects.oid" />
                    </div>
                    <li>id: {{ project.oid }}</li>
                    <li>Date: {{project.createdDate}}</li>
                    <li>Schema: {{project.schema}}</li>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-expansion-panels>
    </div>
    <div v-else>
        <ProjectList :projects="filteredProjects"></ProjectList>
    </div>

</div>
</template>

<script>
import axios from "axios";
import ProjectList from "./ProjectList.vue";
import CheckOutIconButtonVue from "./functionality/buttons/CheckOutIconButton.vue";
import DeleteButtonVue from "./functionality/buttons/DeleteButton.vue";
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
            console.log(this.projects.filter(project => project.oid.toString().includes(this.search.toString()) | project.name.toLowerCase().includes(this.search.toLowerCase())))
            return this.projects.filter(project => project.oid.toString().includes(this.search.toString()) | project.name.toLowerCase().includes(this.search.toLowerCase()))
        }
    },
    components: {
        ProjectList,
        CheckOutIconButtonVue,
        DeleteButtonVue

    }
}
</script>
