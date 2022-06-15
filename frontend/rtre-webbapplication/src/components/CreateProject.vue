<template>
<div>
<v-card class="pb-4">
    <v-form ref="form">
        <v-card-text>
        <v-text-field :rules="ruleInput" v-model="projectName" label="Project Name" outlined class="shrink mx-11"></v-text-field>
        </v-card-text>
        <v-select :rules="ruleSelector" :items="items" v-model="selectedFormat" dense label="Ifc schema" outlined class="mx-11"></v-select>
        <v-btn class="ml-11" color="blue white--text" @click="createProject()" :loading="loading[0]">
            Create Project
        </v-btn>
    </v-form>
</v-card>
    <div v-if="addedProject===true">
        <modal name="CreatedProject">
        Successfully Created New Project<br>
        Project Name: {{ projectData.name }}.<br>
        Project Id: {{ projectData.oid }}<br>
        Project Schema: {{ projectData.schema }} <br>
          <div>
          <br><img src="../assets/Success.png" style="width:200px;height:250px;">
        <!--    <iframe src="../assets/Success.png" width="45%" height="45%" style="position:relative" frameBorder="0"></iframe>-->
            </div>
        <br>
    </modal>

    </div>
    <SnackBar :response="response"></SnackBar>
</div>
</template>

<script>

import Vue from 'vue'
import SnackBar from './functionality/buttons/SnackBar.vue'
import axios from 'axios'
export default {
    name: "CreateProject",
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need a file!'
            ],
            ruleInput: [
                value => !!value || 'input an id'
            ],
            ruleSelector: [
                value => !!value || 'Select a schema'
            ],
            fileForCheckin: '',
            projectName: null,
            selectedFormat: "",
            items: ["Ifc4", "Ifc2x3tc1"],
            fileupload: [],
            response: "",
            loading: [false],
            projectData: [],
            addedProject: false
        };
    },
    methods: {
        async createProject() {
            if (this.$refs.form.validate()) {
                Vue.set(this.loading, 0, true)
                
                this.response = await axios.get("http://localhost:3030/api/CreateProject?" + new URLSearchParams({
                    projectName: this.projectName,
                    schema: this.selectedFormat,
                    token: sessionStorage.getItem("TokenId")
                })).then((resp) => {
                    this.projectData = resp.data
                    this.addedProject = true;
                    return resp;
                });
                
                    
            }
            Vue.set(this.loading, 0, false)
        },
    },
    components: {
        SnackBar
    }
}
</script>
