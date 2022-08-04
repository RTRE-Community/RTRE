<template>
<div id="app">
    <v-expansion-panels v-for="project in projectList" :key="project.oid" popout class="rounded-0">
        <v-expansion-panel v-if="project.parentId == -1" :key="project.id" popout>
            <v-expansion-panel-header color="blue white--text" dark flat>
                {{ project.name }}
                <template v-if="checkIfNewProject(project.oid)">
                    <v-badge inline color="red lighten-4"></v-badge>
                </template>
            </v-expansion-panel-header>
            <v-expansion-panel-content>
                <li>id:{{ project.oid }}</li>
                <li>Date: {{ project.createdDate }}</li>
                <li>Schema: {{project.schema}}</li>
                <v-expansion-panels v-for="subProjects in projectList" :key="subProjects.oid" popout class="rounded-0">
                    <v-expansion-panel v-if="subProjects.parentId == project.oid" :key="subProjects.id"  :name="index++">
                        <v-expansion-panel-header color="blue white--text" dark flat>
                            {{ subProjects.name }}
                            <template v-if="checkIfNewProject(subProjects.oid)">
                                <v-badge inline color="red lighten-4"></v-badge>
                            </template>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <div>
                                <CheckOutIconButton :oid="subProjects.oid" />
                                <ModelViewButton :oid="subProjects.oid"/>
                                <DeleteButton :oid="subProjects.oid" />
                            </div>
                            <li name=id>id: {{ subProjects.oid }} </li>
                            <li>Schema: {{subProjects.schema}}</li>
                            <li>parentId : {{ subProjects.parentId }}</li>
                            <li>Date of Creation : {{ subProjects.createdDate }}</li>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-expansion-panels>
</div>
</template>

<script>
import CheckOutIconButton from "./functionality/buttons/CheckOutIconButton.vue"
import DeleteButton from "./functionality/buttons/DeleteButton.vue"
import ModelViewButton from "./functionality/buttons/ModelViewButton.vue";
export default {
    name: "ProjectList",
    props: ["projects"],
    components: {
    CheckOutIconButton,
    DeleteButton,
    ModelViewButton
},
    data() {
        return {
            index: 1,
            projectList: [],
            messages: 0,
        };
    },
    mounted() {
        this.projectList = this.projects;
           this.projectList.sort(function (a, b) {
            var dateA = new Date(a.createdDate)
            var dateB = new Date(b.createdDate)
            return dateB - dateA
        })
        //const START_DATE = new Date('2019-01-01');

    },
    methods: {
        checkIfNewProject(id) {
            return this.notifications.includes(id.toString())
        },
        orderedSubprojects(projects) {
            return projects.subProjects.filter()
        }
    },
    computed: {
        notifications() {
            return this.$store.state.Notification
        }
    }
};
</script>

<style></style>
