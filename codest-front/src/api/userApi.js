import {userApi} from "@/api/api";

export default {
    register(username,password) {
        return userApi.post("/users",{
            username: username,
            password: password
        })
    },
    login(username,password) {
        return userApi.post("/auth/login",{
            username: username,
            password: password
        })
    }
}