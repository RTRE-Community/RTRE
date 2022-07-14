<template>
<v-container class="mt-16">
    <v-layout row>
        <v-flex xs12 md6>
            <v-container class="mb-12">
                <FunctionHub></FunctionHub>
            </v-container>
        </v-flex>
        <v-flex xs12 md6>
            <div v-if="!showOverlay">
            <v-container>
                <SearchBox :key="componentKey"></SearchBox>
            </v-container>
            </div>
        </v-flex>
        <div v-if="this.users !== null">
            <v-container>
               <ChatWIndow @click="this.showOverlay=true" :overlay="this.showOverlay" :allUsers="this.users" :userMessages="this.userMessages" :key="this.componentKey"/>
            </v-container></div>
        <v-btn name="refresh" @click="forceRerender()">Refresh</v-btn>
    </v-layout>
</v-container>
</template>

<script>
import SearchBox from "@/components/SearchBox.vue";
import FunctionHub from "@/components/FunctionHub.vue";
import ChatWIndow from '../components/ChatWIndow.vue'
const EventEmitter = require('../EventEmitter')
import axios from 'axios'

export default ({
    name: 'MainHub',
    components: {
        SearchBox,
        FunctionHub,
        ChatWIndow,
    },
    created(){
        EventEmitter.eventEmitter.on('disableProjectBox', this.disableProjectBox);
        EventEmitter.eventEmitter.on('enableProjectBox', this.enableProjectBox);
       

        if(sessionStorage.getItem('UserType') === 'ADMIN'){
            console.log(sessionStorage.getItem("TokenId"));
            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getAllUsers?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId")
            })).then((resp) => {
                //localStorage.setItem("Users", resp.data);
                this.dupUsers = resp.data;
                for(let i = 0; i< this.dupUsers.length; i++){
                    for(let j = 0; j < this.dupUsers.length; j++){
                        if(this.dupUsers[i].oid === this.dupUsers[j].oid){
                            this.dupUsers.splice(i, 1);
                        }
                    }
                }
                localStorage.setItem("Users", JSON.stringify(this.dupUsers));
                console.log(resp.data);
            });

        } else {
            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getAllUsers!Admin?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId")
            })).then((resp) => {
                //this.users = resp.data
                this.dupUsers = resp.data;
                for(let i = 0; i< this.dupUsers.length; i++){
                    for(let j = 0; j < this.dupUsers.length; j++){
                        if(this.dupUsers[i].oid === this.dupUsers[j].oid){
                            this.dupUsers.splice(i, 1);
                        }
                    }
                }
                //let uniq = [...new Set(users)];

                //let uniqueChars = users.filter((element, index) => {
                 //return users.indexOf(element) === index;
                //});

                localStorage.setItem("Users", JSON.stringify(this.dupUsers));
                console.log(this.dupUsers);
            });
        }
    },
    data() {
    return {
        showOverlay: null,
        users: [],
        componentKey: 0,
        userMessages: [],
        dupUsers: [],
        Querys: []

      
      } // specifies the color scheme for the component
     
    },
  methods: {
    disableProjectBox(){
        this.showOverlay = true;
        //console.log('overlay is true')
    },
    enableProjectBox(){
        this.showOverlay = false;
       // console.log('overlay is false')

    },
    forceRerender(){
        this.componentKey += 1;
      }  
    
    }
})
</script>

<style>

</style>