<template>
<div>
    <v-card class="pb-4">
        <v-form ref="form">
            <v-card-text>
                <v-text-field :rules="ruleInput" v-model="parent0Id" label="Head Project ID" outlined class="shrink mx-11"></v-text-field>
            </v-card-text>
            <v-btn class="ml-11" color="blue white--text" @click="getUsers()" :loading="loading[0]">View Users</v-btn>
            <div v-if="this.showTable==true">
                <div v-for="user in users" v-bind:key="user.oid">
                    <v-list-item dense>
                        <v-list-item-content>
                            <v-list-item-title>Name: {{ user.name }}</v-list-item-title>
                            <v-list-item-title>Email: {{ user.username }}</v-list-item-title>
                            <v-list-item-title>Id: {{ user.oid }}</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </div>
            </div>
        </v-form>
    </v-card>
</div>
</template>

<script>
import axios from "axios";

export default {
    name: "ViewUsers",

    data() {
        return {
            file: null,
            parent0Id: null,
            users: [],
            showTable: false,
            ruleInput: [
                value => !!value || 'input an id'
            ],
            loading: [false],
        };
    },
    methods: {
        async getUsers() {
            axios.get('http://host.docker.internal:3030/api/ViewUsers?' + new URLSearchParams({
                parent0Id: this.parent0Id,
                token: sessionStorage.getItem("TokenId")
            })).then((resp) => {
                this.showTable = true
                this.users = resp.data
            });
        }
    },
    mounted() {

    }
};
</script>

<style></style>
