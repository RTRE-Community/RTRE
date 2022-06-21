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
               <ChatWIndow @click="this.showOverlay=true" :overlay="this.showOverlay" :allUsers="this.users" :userMessages="this.userMessages" />
            </v-container></div>
        <v-btn @click="forceRerender()">Refresh</v-btn>
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
        ChatWIndow
    },
    created(){
        EventEmitter.eventEmitter.on('disableProjectBox', this.disableProjectBox);
        EventEmitter.eventEmitter.on('enableProjectBox', this.enableProjectBox);

        axios.get('http://localhost:3030/api/getUserMessages?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                username: sessionStorage.getItem('Username')
            })).then((resp) => {
                this.userMessages = resp.data
                console.log(resp.data);

            });

        if(sessionStorage.getItem('UserType') === 'ADMIN'){
            console.log(sessionStorage.getItem("TokenId"));
            axios.get('http://localhost:3030/api/getAllUsers?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId")
            })).then((resp) => {
                this.users = resp.data

                if(this.users.length < 1){
                    console.log('empty user list');
                }
                //console.log(this.users);

            });

        } else {
            axios.get('http://localhost:3030/api/getAllUsers!Admin?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId")
            })).then((resp) => {
                this.users = resp.data

             
                console.log(this.users);
                console.log('after request')

            });

        }
        

    },
    data() {
    return {
        showOverlay: null,
        users: [],
        componentKey: 0,
        userMessages: []
      
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