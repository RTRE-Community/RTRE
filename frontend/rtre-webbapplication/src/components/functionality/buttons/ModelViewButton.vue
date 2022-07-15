<template>
<v-tooltip bottom>
    <template v-slot:activator="{on , attrs}">
        <v-btn class="mx-2" fab dark small v-bind="attrs" v-on="on" color="grey" @click="installWithOid()">

            <v-icon dark>
                mdi-eye-outline
            </v-icon>
        </v-btn>
    </template>
    <span>Check out</span>
</v-tooltip>
</template>

<script>
import Axios from "axios"
export default {
    name: "ModelViewButton",
    props: ['oid'],
    methods: {
        installWithOid() {
            Axios({
                    url: process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + this.oid,
                    methods: "GET",
                })
                .then((res) => {
                    if (sessionStorage.getItem(this.oid) != null) {
                        console.log('here')
                        this.$router.push({
                            name: "Viewer",
                            params: {
                                oid: this.oid
                            }
                        })
                    } else {
                        console.log('there')
                        sessionStorage.setItem(this.oid, res.data)
                        this.$router.push({
                            name: "Viewer",
                            params: {
                                oid: this.oid
                            }
                        })
                    }
                });
        }
    },
}
</script>
