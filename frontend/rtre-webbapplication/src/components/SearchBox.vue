<template>
<div>
    <v-text-field solo label="Search" prepend-inner-icon="mdi-magnify" v-model="search" elevation="24" clearable outlined @click:clear="resetFilteredProjects()"></v-text-field>

    <div v-if="search !== ''">
        <v-expansion-panels v-for="project in filteredProjects" :key="project.id" popout class="rounded-0">
            <v-expansion-panel>
                <v-expansion-panel-header color="blue white--text" dark flat>
                    {{project.name}}
                    <v-spacer></v-spacer>
                    <template v-if="checkIfNewProject(project.oid)">
                        <v-badge inline color="red lighten-4"></v-badge>
                    </template>
                    <div class="d-flex flex-row-reverse" v-if="project.parentId == -1">
                        <MoreOptionsButton></MoreOptionsButton>
                    </div>
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                    <div v-if=" project.parentId != -1" class="py-2">
                        <CheckOutIconButtonVue :oid="projects.oid" :schema="projects.schema" />
                        <ModelViewButton :oid="projects.oid" />
                        <DeleteButtonVue :oid="projects.oid" />
                    </div>
                    <li>id: {{ project.oid }}</li>
                    <li>Date: {{project.createdDate}}</li>
                    <li>Schema: {{project.schema}}</li>
                    <li v-if="project.description"> Description : <p> {{project.description}}</p>
                    </li>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-expansion-panels>
    </div>
    <div v-else>
        <ProjectList :key="filteredProjects.length" :projects="filteredProjects"></ProjectList>
    </div>

</div>
</template>

<script>
import axios from "axios";
import ProjectList from "./ProjectList.vue";
import CheckOutIconButtonVue from "./functionality/buttons/CheckOutIconButton.vue";
import DeleteButtonVue from "./functionality/buttons/DeleteButton.vue";
import ModelViewButton from "./functionality/buttons/ModelViewButton.vue";
import MoreOptionsButton from "./functionality/buttons/MoreOptionsButton.vue";
export default {
    name: "SearchBox",
    data() {
        return {
            projects: [],
            search: ''
        };
    },
    mounted() {
        this.fetchProjectList()
        this.projects.sort(function (a, b) {
            var dateA = new Date(a.createdDate)
            var dateB = new Date(b.createdDate)
            return dateB - dateA
        })
        this.search = ""
    },
    methods: {
        checkIfNewProject(id) {
            return this.notifications.includes(id.toString())
        },
        resetFilteredProjects() {
            this.search = ""
        },
        fetchProjectList() {
            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getProjectList?token=" + sessionStorage.getItem('TokenId')).then((resp) => {
                this.projects = resp.data;
            });
        }
    },
    computed: {
        filteredProjects() {
            if (this.search === "") {
                return this.projects
            } else {
                return this.projects.filter(project => project.oid.toString().includes(this.search.toString()) | project.name.toLowerCase().includes(this.search.toLowerCase()))
            }
        },
        notifications() {
            return this.$store.state.Notification
        }
    },
    components: {
        ProjectList,
        CheckOutIconButtonVue,
        DeleteButtonVue,
        ModelViewButton,
        MoreOptionsButton
    },

}
</script>
