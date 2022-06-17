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
                <v-list-item-content>
                    <v-list-item-title>Newly added project!</v-list-item-title>
                    <v-list-item-subtitle> with the id {{notification}}</v-list-item-subtitle>
                    <v-divider></v-divider>
                </v-list-item-content>
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
export default ({
    name: "NotificationHub",
    data() {
        return {
            notifications: [],
            polling: null
        }
    },
    created() {
        this.polldata()
    },
    methods: {
        polldata() {
            this.polling = setInterval(() => {
                this.fetchNotifications()
            }, 3000)
        },
        fetchNotifications() {
            axios.get("http://localhost:3030/api/getAllNotification?", {
                params: {
                    username: sessionStorage.getItem('Username'),
                    uuid: sessionStorage.getItem('uuid')
                }
            }).then((resp) => {
                this.notifications = resp.data.postId
            })
        },
        deleteNotification(notificationId) {
            axios.delete("http://localhost:3030/api/deleteNotification?", {
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
