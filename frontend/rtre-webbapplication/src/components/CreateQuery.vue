<template>
<div>
    <v-card class="pb-4">
        <v-form ref="form">
            <v-card-text>
                <v-text-field :rules="ruleInput"  label="Project Name" outlined class="shrink mx-11"></v-text-field>
            </v-card-text>
            <v-select :rules="ruleSelector" :items="items" v-model="selectedFormat" dense label="Ifc Query" outlined class="mx-11"></v-select>
            <v-btn class="ml-11" color="blue white--text" @click="getQuery()" :loading="loading[0]">
                Generate Query
            </v-btn>
            <v-btn class="ml-11" color="blue white--text" @click="AssignUser()">
                Assign Query to User
            </v-btn>
        </v-form>
    </v-card>
    <div v-if="this.overlay">
         <v-overlay
         :value="overlay"
         >
         <h1>Select User to Assign Query</h1><br>
           <v-btn color="warning"
             :value="overlay"
             @click="overlay=false">Cancel</v-btn> <br><br><br>
        <v-list >
      <v-subheader>USERS</v-subheader>
      <v-list-item-group v-model="Users"
       color="primary">
        <v-list-item 
          v-for="User in Users"
          :key="User.oid"
          @click="selectUser(User)"
        >
          <v-list-item-icon>
            <v-icon v-text="User.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="User.name"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>

         </v-overlay>
    </div>
    <SnackBar :response="this.response"></SnackBar>
</div>
</template>

<script>
import Vue from 'vue'
import SnackBar from './functionality/buttons/SnackBar.vue'
import axios from 'axios'
import json from '../assets/Querys.json'

export default {
    name: "CreateQuery",
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need a Query'
            ],
            ruleInput: [
                value => !!value || 'input an id'
            ],
            ruleSelector: [
                value => !!value || 'Select a query'
            ],
            selectedFormat: "",
            items: [],
            response: 0,
            loading: [false],
            Query: {},
            overlay: false,
            Users: []
        };
    },
    created() {
        for(let i = 0; i < json.names.length; i++){
            this.items[i] = json.names[i];
        }
        this.Users = JSON.parse(localStorage.getItem("Users") || "[]")

        for(let i = 0; i < this.Users.length; i++){

            if(this.Users[i].oid === parseInt(sessionStorage.getItem('oid'))){

                this.Users.splice(i, 1);

            }else {
               // this.Users.setQuery = {};
            }
        }
        
    },
    watch: {
        selectedFormat(){
            console.log(this.selectedFormat);

        }
        
    },
    methods: {

        async selectUser(User){
            this.User = User;
            const id = User.oid;
            this.overlay = false;
            this.Query = await axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getQuerys?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                queryName: this.selectedFormat
            })).then((resp) => {
                return resp.data;
               
            });
            axios.post(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/sendQuery?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                receievingUser: this.User.oid,
                queryTopic: this.selectedFormat,
                Query: JSON.stringify(this.Query)
            })).then((resp) => {
                if(resp.status===200){
                    console.log(200);
                }
                const index = this.getIndex(id);

                this.Users[index].Query = {};
                this.Users[index].Query = this.Query;

                localStorage.setItem("Users", JSON.stringify(this.Users));


                //axios.post(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/sendQuery?)
            });
        },
        AssignUser(){
            this.overlay = true;

        },
        getIndex(id){
      
            const index = this.Users.map(e => e.oid).indexOf(id);
            return index;
      
        },
        async getQuery() {
            Vue.set(this.loading, 0, true)
               axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getQuerys?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                queryName: this.selectedFormat
            })).then((resp) => {
                this.Query = resp.data;
                this.response = resp;
                console.log(resp.status);
               
            });
            Vue.set(this.loading, 0, false);

          
        },
    },
    components: {
        SnackBar
    }
}
</script>
