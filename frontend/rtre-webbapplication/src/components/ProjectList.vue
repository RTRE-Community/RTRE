<template>
<div id="app">
    <v-expansion-panels v-for="project in projectList" :key="project.oid" popout class="rounded-0">
        <v-expansion-panel v-if="project.parentId == -1" :key="project.id" popout>
            <v-expansion-panel-header color="blue white--text" dark flat>
                {{ project.name }}
                <template v-if="checkIfNewProject(project.oid)">
                    <v-badge inline color="red"></v-badge>
                </template>
            </v-expansion-panel-header>
            <v-expansion-panel-content>
                <li>id:{{ project.oid }}</li>
                <li>Date: {{ project.createdDate }}</li>
                <li>Schema: {{project.schema}}</li>
                <v-expansion-panels v-for="subProjects in projectList" :key="subProjects.oid" popout class="rounded-0">
                    <v-expansion-panel v-if="subProjects.parentId == project.oid" :key="subProjects.id">
                        <v-expansion-panel-header color="blue white--text" dark flat>
                            {{ subProjects.name }}
                            <template v-if="checkIfNewProject(subProjects.oid)">
                                <v-badge inline color="red lighten-4"></v-badge>
                            </template>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <div>
                                <CheckOutIconButton :oid="subProjects.oid" />
                                <DeleteButton :oid="subProjects.oid" />

                            </div>
                            <li>id: {{ subProjects.oid }} </li>
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
export default {
    name: "ProjectList",
    props: ["projects"],
    components: {
        CheckOutIconButton,
        DeleteButton,
    },
    data() {
        return {
            projectList: [],
            messages: 0,
        };
    },
    mounted() {
        this.projectList = this.projects;
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
