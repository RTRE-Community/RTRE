<template>
<v-tooltip bottom>
    <template v-slot:activator="{on , attrs}">
        <v-btn class="mx-2" fab dark small v-bind="attrs" v-on="on" color="green" @click="installWithOid()">

            <v-icon dark>
                mdi-cloud-download
            </v-icon>
        </v-btn>
    </template>
    <span>Check out</span>
</v-tooltip>
</template>

<script>
import FileDownload from "js-file-download"
import Axios from "axios"
export default {
    name: "CheckOutIconButton",
    props: ['oid'],
    methods: {
        installWithOid() {
            Axios({
            url:  process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + this.oid,
            methods:"GET",
            responseType:"blob"
                })
            .then((res) => {FileDownload(res.data,"myIfcFile.ifc")});
        }
    },
}
</script>
