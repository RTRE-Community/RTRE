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
    props: ['oid', 'schema'],
    methods: {
        installWithOid() {
            var correctFormatSchema = this.schema.charAt(0).toUpperCase() + this.schema.slice(1)
            console.log(correctFormatSchema)
            Axios({
            url:  "http://localhost:3030/api/getIfc?fileName=" + this.oid + "&schema=" + correctFormatSchema,
            methods:"GET",
            responseType:"blob"
                })
            .then((res) => {FileDownload(res.data,"myIfcFile.ifc")});
        }
    },
}
</script>
