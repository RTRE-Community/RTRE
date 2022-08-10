<template>
<v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
                <v-icon dark>
                    mdi-cloud-download
                </v-icon>
            </v-btn>
        </template>
        <v-card>
    <v-toolbar flat color="blue" dark class="rounded-0">
        <v-toolbar-title>User Profile</v-toolbar-title>
          <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
          >
            Close
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
          >
            Save
          </v-btn>
        </v-card-actions>
    </v-toolbar>
    <v-tabs vertical>
        <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Check in
        </v-tab>
        <v-tab>
            <v-icon left> mdi-cloud-download </v-icon>
            Check Out
        </v-tab>
        <v-tab>
            <v-icon left> mdi-call-merge </v-icon>
            Merge
        </v-tab>
        <v-tab-item>
            <CheckIn/>
        </v-tab-item>
        <v-tab-item>
            <CheckOut/>
        </v-tab-item>
        <v-tab-item>
           <Merge/>
        </v-tab-item>
    </v-tabs>
</v-card>
    </v-dialog>
</v-row>
</template>

<script>
import CheckIn from '../CheckIn.vue';
import CheckOut from '../CheckOut.vue';
import Merge from '../Merge.vue';
export default {
    name: "ModelViewButton",
    props: ["oid"],
    data: () => ({
        dialog: false,
    }),
    components: { CheckIn, CheckOut, Merge }
}
</script>
