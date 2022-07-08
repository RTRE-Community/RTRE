<template>
<div>
    <div v-if="!presentQuery">
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <v-text-field :rules="ruleInput" label="id" v-model="id" outlined class="shrink mx-11"></v-text-field>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut" :loading="loading[0]">Get Project</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>
</div>
<div v-if="presentQuery">
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <v-text-field :rules="ruleInput" label="id" v-model="id" outlined class="shrink mx-11"></v-text-field>
             <v-select
        :rules="ruleSelector"
        :items="items"
        v-model="selectedQuery"
        dense
        label="Available IFC queries"
        outlined
        class="mx-11"
      ></v-select>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut" :loading="loading[0]">Get Project</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>

</div>
</div>


</template>

<script>
import FileDownload from "js-file-download"
import Axios from "axios"
import SnackBar from "./buttons/SnackBar.vue";
import Vue from 'vue'
import axios from 'axios'
//const EventEmitter = require('../../EventEmitter')
export default {
    name: "checkOut",
    data() {
        return {
            ruleInput: [
                value => value >= 3 || 'input an id'
            ],
            id: "",
            response: "",
            loading: [false],
            presentQuery: false,
            ruleSelector: [(value) => !!value || "Select a Query"],
            query: {},
            items: [],
            selectedQuery: '',
            Querys: []
        };
    },
    created(){

        //this.Querys = JSON.parse(localStorage.getItem("Querys") || "[]")
        this.Querys = JSON.parse(sessionStorage.getItem('Querys'));
        for(let i = 0; i< this.Querys.length; i++) {
            this.items.push(this.Querys[i].type.name);
            //this.items[i] = this.Querys[i].type.name;
        }
        console.log(this.Querys);
        if(this.items.length > 0){
            this.presentQuery = true;
        }else {
            this.presentQuery = false;
        }
        
    },
    methods: {
        async checkOut() {
            if (this.$refs.form.validate() && !this.presentQuery) {
                let that = this;
                Vue.set(this.loading, 0, true)
                console.log('no query')
                await Axios({
                        url: process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + this.id,
                        methods: "GET",
                        responseType: "blob"
                    })
                    .then((res) => {
                        FileDownload(res.data, "myIfcFile.ifc")
                        this.response = res
                        console.log(this.response)
                    })
                    .catch(function (error) {
                        that.response = error.response
                    });
                    Vue.set(this.loading, 0, false)

            } else {
                let query = {};
                console.log(this.selectedQuery);
                for(let i = 0; i < this.Querys.length; i++){
                    console.log(this.Querys[i].type.name);
                    if(this.Querys[i].type.name === this.selectedQuery){
                        query = this.Querys[i];
                        console.log(this.Querys[i]);
                    }
                }
                console.log(query);
                let that = this;
                Vue.set(this.loading, 0, true)
                axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getIfc?' + new URLSearchParams({
                fileName: this.id,
                query: JSON.stringify(query)
            })).then((resp) => {
                FileDownload(resp.data, "myIfcFile.ifc")
                    this.response = resp
                    console.log(this.response)
            })
            .catch(function (error) {
                        that.response = error.response
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
