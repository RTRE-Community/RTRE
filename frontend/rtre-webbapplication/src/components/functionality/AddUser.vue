<template>
<v-card class="pb-4">
    <v-form ref="form">
        <v-card-text>
            <v-text-field :rules="ruleInput" v-model="parent0Id" label="Head Project ID" outlined class="shrink mx-11"></v-text-field>
        </v-card-text>
        <v-text-field :rules="ruleInput" v-model="username" label="User Email" outlined class="shrink mx-11"></v-text-field>
        <div>
            <v-btn v-if="removeUser[0]==false" class="ml-11" color="blue white--text" @click="addUserApi()" :loading="loading[0]">
                Submit
            </v-btn>
        </div>
        <div>
            <v-btn v-if="removeUser[0]==true" class="ml-11" color="blue white--text" @click="removeUserApi()" :loading="loading[0]">
                Submit
            </v-btn>
        </div>
    </v-form>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import SnackBar from './buttons/SnackBar.vue';
import Vue from 'vue'
export default {
    name: "AddUser",
    props: ['mode'],
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need input!'
            ],
            ruleInput: [
                value => !!value || 'input an id'
            ],
            parent0Id: null,
            username: null,
            loading: [false],
            addedUser: false,
            response: null,
            data: {},
            removeUser: [false],
            removedUser: null
        };
    },
    mounted() {
        if (this.mode == true) {
            Vue.set(this.removeUser, 0, false)
        } else {
            Vue.set(this.removeUser, 0, true)
        }
    },
    methods: {
        async addUserApi() {
            if (this.$refs.form.validate()) {
                Vue.set(this.loading, 0, true)
                this.response = await fetch(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/AddUserToProject?' + new URLSearchParams({
                    parent0Id: this.parent0Id,
                    username: this.username,
                    token: sessionStorage.getItem("TokenId")
                })).then((resp) => {
                    return resp;
                });

                if (this.response.status === 200) {
                    this.addedUser = true;
                }
            }
            Vue.set(this.loading, 0, false)
        },
        async removeUserApi() {
            if (this.$refs.form.validate()) {
                Vue.set(this.loading, 0, true)
                this.response = await fetch(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/RemoveUserFromProject?' + new URLSearchParams({
                    parent0Id: this.parent0Id,
                    username: this.username,
                    token: sessionStorage.getItem("TokenId")
                })).then((resp) => {
                    return resp;
                });

                if (this.response.status === 200) {
                    this.removedUser = true;
                }
            }
            Vue.set(this.loading, 0, false)
        }
    },
    components: {
        SnackBar
    }
}
</script>
