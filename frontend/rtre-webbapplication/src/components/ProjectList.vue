<template>
<div id="app">
    <v-expansion-panels v-for="project in projectList" :key="project.oid" popout class="rounded-0">
        <v-expansion-panel v-if="project.parentId == -1" :key="project.id" popout>
            <v-expansion-panel-header color="blue white--text" dark flat>
                <v-row>
                    <v-col xs="12" md="6">
                        <p   style="word-break: break-word"  class="text-wrap">
                            {{ project.name }}
                        </p>
                        <v-spacer></v-spacer>
                    </v-col>
                    <v-col xs="12" md="6">
                        <template v-if="checkIfNewProject(project.oid)">
                            <v-badge inline color="red lighten-4"></v-badge>
                        </template>
                        <div class="d-flex flex-row-reverse">
                            <MoreOptionsButton :oid="project.oid"></MoreOptionsButton>
                        </div>
                    </v-col>
                </v-row>
            </v-expansion-panel-header>
            <v-expansion-panel-content>
                <li>id:{{ project.oid }}</li>
                <li>Date: {{ project.createdDate }}</li>
                <li>Schema: {{project.schema}}</li>
                <v-expansion-panels v-for="subProjects in projectList" :key="subProjects.oid" popout class="rounded-0">
                    <v-expansion-panel v-if="subProjects.parentId == project.oid" :key="subProjects.id" :name="index++">
                        <v-expansion-panel-header color="blue white--text" dark flat>
                            {{ subProjects.name.split('-')[0] }}
                            <p class="font-weight-thin">{{ subProjects.name.split('-')[1]}}</p>
                            <template v-if="checkIfNewProject(subProjects.oid)">
                                <v-badge inline color="red lighten-4"></v-badge>
                            </template>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <div class="py-2">
                                <CheckOutIconButton :oid="subProjects.oid" />
                                <ModelViewButton :oid="subProjects.oid" />
                                <DeleteButton :oid="subProjects.oid" />
                            </div>
                            <li name=id>id: {{ subProjects.oid }} </li>
                            <li>Schema: {{subProjects.schema}}</li>
                            <li>parentId : {{ subProjects.parentId }}</li>
                            <li>Date of Creation : {{ subProjects.createdDate }}</li>
                            <li v-if="subProjects.description"> Description : <p> {{subProjects.description}}</p>
                            </li>
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
import MoreOptionsButton from "./functionality/buttons/MoreOptionsButton.vue";
export default {
    name: "ProjectList",
    props: ["projects"],
    components: {
        CheckOutIconButton,
        DeleteButton,
        ModelViewButton,
        MoreOptionsButton
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
