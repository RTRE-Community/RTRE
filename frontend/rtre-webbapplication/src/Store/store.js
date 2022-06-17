import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
    state:{
        Notification:[]
    },
    mutations:{
        updateNotification: (state,payload) =>{
            state.Notification = payload
        },
        includeId:(state, payload) => {
            return state.Notification.includes(payload)
        }
    },
    actions:{
        updateNotification:(context, payload) => {
            context.commit('updateNotification', payload)
        },
        includeId:(context, payload) =>{
            return context.commit('includeId', payload)
        }
    }
})