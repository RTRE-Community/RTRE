<template>
<v-card>
    <v-toolbar flat color="blue" dark class="rounded-0">
        <v-toolbar-title>User Profile</v-toolbar-title>
    </v-toolbar>
    <v-tabs vertical>
        <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Create New Project 
        </v-tab>
        <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Add User to Project
        </v-tab>
        <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Remove User from Project
        </v-tab>
        <v-tab >
            <v-icon left> mdi-archive-arrow-up </v-icon>
            View Users of Project
        </v-tab>
        <v-tab-item>
            <CreateProject />
        </v-tab-item>
        <v-tab-item>
            <AddUser :mode="true"/>
        </v-tab-item>
        <v-tab-item>
            <AddUser :mode="false"/>
        </v-tab-item>
        <v-tab-item>
            <ViewUsers />
        </v-tab-item>
    </v-tabs>
</v-card>
</template>

<script>
import AddUser from "./functionality/AddUser.vue"
import ViewUsers from './ViewUsers.vue'
import CreateProject from './CreateProject.vue'

export default {
    components: {
        AddUser,
        ViewUsers,
        CreateProject
    },
    name: "FunctionHub",
    data() {
    return {
        mode: null,
    }
    },
    methods: {
        ToggleOption(toggle) {
            if(toggle === true){
                this.mode = true;
            } else {
                this.mode = false;
            }
        }
    }
    
    
};
</script>

<style></style>
