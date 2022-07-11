<template>
<div>
    <v-card class="pb-4">
        <v-form ref="form">
         <br>
            <v-select :rules="ruleSelector" :items="items" v-model="selectUser" dense label="Select User to manage" outlined class="mx-11"></v-select>
            <v-btn class="ml-11" color="blue white--text" @click="getQuery()" :loading="loading[0]">
                View User Queries
            </v-btn>
            <v-btn class="ml-11" color="blue white--text" @click="RemoveQuery()">
                Remove Query from User
            </v-btn>
        </v-form>
    </v-card>
    <div v-if="this.overlay">
       <v-list shaped>
      <v-subheader>QUERIES</v-subheader>
      <v-list-item-group
        v-model="selectedQuery"
        color="primary"
      >
        <v-list-item
          v-for="query in Queries"
          :key="query"
        >
          <v-list-item-content>
            <v-list-item-title v-text="query" ></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
    </div>
    <SnackBar :response="this.response"></SnackBar>
</div>
</template>

<script>
import Vue from 'vue'
import SnackBar from './functionality/buttons/SnackBar.vue'
import axios from 'axios'
import json from '../assets/Querys.json'
//const EventEmitter = require('../EventEmitter')

export default {
    name: "ManageQueries",
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need a User'
            ],
            ruleInput: [
                value => !!value || 'input an id'
            ],
            ruleSelector: [
                value => !!value || 'Select a query'
            ],
            selectUser: "",
            items: [],
            response: 0,
            loading: [false],
            Queries: [],
            overlay: false,
            Users: [],
            selectedQuery: ''
        };
    },
    created() {
        
        this.Users = JSON.parse(localStorage.getItem("Users") || "[]")
        console.log(this.Users);

        for(let i = 0; i < this.Users.length; i++){

            if(this.Users[i].oid === parseInt(sessionStorage.getItem('oid'))){

                this.Users.splice(i, 1);

            }else {
               // this.Users.setQuery = {};
            }
        }
        
        for(let i = 0; i < json.names.length; i++){
            this.items[i] = this.Users[i].name;
        }

        axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getUserQuerys?' + new URLSearchParams({
            username: this.selectUser,
            oid: this.selectUser
        })).then((resp) => {
            this.Queries = resp.data;

            });

        
    },
    watch: {
        selectedFormat(){
            console.log(this.selectedFormat);

        },
        selectedQuery(){
            console.log(this.Queries[this.selectedQuery]);
        }
    },
    methods: {
        RemoveQuery(){
            let index = this.getIndex(this.selectUser);
            axios.delete(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/deleteUserQuery?' + new URLSearchParams({
            oid: this.Users[index].oid,
            queryName: this.Queries[this.selectedQuery]
        })).then((resp) => {
            console.log(resp.status);
            });

        },
        getIndex(name){
      
            const index = this.Users.map(e => e.name).indexOf(name);
            return index;
      
        },
        async getQuery() {
            Vue.set(this.loading, 0, true)
            let index = this.getIndex(this.selectUser);
            console.log(this.selectUser);
               axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getUserQuerys?' + new URLSearchParams({
                oid: this.Users[index].oid
            })).then((resp) => {
                this.Queries = resp.data;
                this.overlay = true;
                console.log(this.Queries);
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
