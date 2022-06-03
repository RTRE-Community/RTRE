<template>
<v-snackbar v-model="snackbar" :timeout="timeout" :color="color">
    {{ text }}

    <template v-slot:action="{ attrs }">
        <v-btn color="white" text v-bind="attrs" @click="snackbar = false">
            Close
        </v-btn>
    </template>
</v-snackbar>
</template>

<script>
export default ({
    name: "SnackBar",
    data() {
        return{
            snackbar:false,
            timeout:2000,
            color: null,
            text: null,
            tempResponse:null
        }
    },
    props: ['response'],
    watch: {
        response:function(){
           console.log(this.response.status)
            if(this.response.status == 200){
                this.snackbar = true
                this.color = "success"
                this.text = "success"
            }else{
                this.snackbar = true
                this.color = "error"
                this.text = "Error"
            }

        },
        deep:true,
        immediate:true,
        flush:'post'
    },
})
</script>
