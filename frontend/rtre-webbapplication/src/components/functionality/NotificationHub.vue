<template>
<div class="text-center pt-4">
    <v-menu transition="fade-transition" :offset-y="true" :close-on-click="false">
        <template v-slot:activator="{on, attrs}">
            <v-btn depressed rounded color="error" v-bind="attrs" v-on="on" :disabled="notifications.length <= 0">
                <v-icon>mdi-bell</v-icon>
                Alert
            </v-btn>
        </template>
        <v-list>
            <v-list-item v-for="(notification, index) in notifications" :key="index">
             <div v-if="projectAlert===true">
                <v-list-item-content>
                    <v-list-item-title>Newly added project!</v-list-item-title>
                    <v-list-item-subtitle> with the id {{notification}}</v-list-item-subtitle>
                    <v-divider></v-divider>
                </v-list-item-content>
                </div>
                 <div v-if="projectAlert===false">
                <v-list-item-content>
                    <v-list-item-title>You have been assigned a Query!</v-list-item-title>
                    <v-list-item-subtitle> See project for more details</v-list-item-subtitle>
                    <v-divider></v-divider>
                </v-list-item-content>
                </div>
                <v-btn icon @click="deleteNotification(notification)">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-list-item>
        </v-list>
    </v-menu>
</div>
</template>

<script>
import axios from 'axios'
import EventEmitter from '../../EventEmitter'
export default ({
    name: "NotificationHub",
    data() {
        return {
            notifications: [],
            polling: null,
            projectAlert: false,
            Query: {},
            gettedQuerys: [],
            persistentQuerys: []
        }
    },
    created() {
        this.polldata()
    },
    methods: {
        polldata() {
                if(sessionStorage.getItem('TokenId') !== null){
                this.fetchNotifications()
                this.getQuery();
                 }
        },
        fetchNotifications() {
            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getAllNotification?", {
                params: {
                    username: sessionStorage.getItem('Username'),
                    uuid: sessionStorage.getItem('uuid')
                }
            }).then((resp) => {
                this.notifications = resp.data.postId

                if(this.notifications === 0){
                    this.projectAlert = false;
                    //this.Query = resp,data.Query
                }else {
                    this.projectAlert = true;
                }
                this.$store.dispatch('updateNotification', resp.data.postId)
            })
        },
        getQuery(){

            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getUserQuerys?", {
                params: {
                    oid: sessionStorage.getItem('oid')
                }
            }).then((resp) => {

                // this.Query = resp,data.Query
                //console.log(JSON.parse(resp.data[1]));
                if(resp.data.length > 0){
                    for(let i = 0; i < resp.data.length; i++) {
                         if(!(this.containsObject(JSON.parse(resp.data[i][1]), this.gettedQuerys))){
                            //let query = {}
                            let query = JSON.parse(resp.data[i][1]);
                            query.name = resp.data[i][0];
                            query.type.name = resp.data[i][0];
                            
                            this.gettedQuerys.push(query);
                            
                             
                        }

                    }
                    //this.loadQuerys();
                    sessionStorage.setItem('Querys', JSON.stringify(this.gettedQuerys));
                }
                //this.notifications = resp.data;
           
                //this.$store.dispatch('updateNotification', resp.data)
            })

        },
        loadQuerys(){
            for(let i = 0; i < this.gettedQuerys.length; i++){
                if(!(this.containsObject(this.gettedQuerys[i], this.persistentQuerys ))){
                    console.log(this.gettedQuerys[i])
                    this.persistentQuerys.push(this.gettedQuerys[i]);
                }

                /*
                axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getQuerys?" + new URLSearchParams({
                token: sessionStorage.getItem('TokenId'),
                queryName: JSON.stringify(this.gettedQuerys[0][i])
                }
                )).then((resp) => {
                console.log('query getted')
                if(!(this.containsObject(resp.data, this.persistentQuerys ))){
                    this.persistentQuerys.push(resp.data);
                }
                // this.Query = resp.data.Query 
            })
            */
            }
            //localStorage.setItem('Querys', JSON.stringify(this.persistentQuerys));
            EventEmitter.eventEmitter.emit('QuerySetted', this.persistentQuerys);
        
        },
        containsObject(obj, list) {
            let i;
            for (i = 0; i < list.length; i++) {
                if(JSON.stringify(list[i]) === JSON.stringify(obj)) {
                        
                    return true;
                }
            }
            return false;
        },
        deleteNotification(notificationId) {
            axios.delete(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/deleteNotification?", {
                params: {
                    uuid: sessionStorage.getItem('uuid'),
                    username: sessionStorage.getItem('Username'),
                    postId: notificationId
                }
            }).then((resp) => {
                console.log(resp.data)
            })
        }
    },
    beforeDestroy() {
        clearInterval(this.polling)
    }
})
</script>
