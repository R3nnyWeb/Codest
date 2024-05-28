import {userApi} from "@/api/api";

export default {
    register(username,password) {
        return userApi.post("/v1/users",{
            username: username,
            password: password
        })
    },
    login(username,password) {
        return userApi.post("/v1/users/auth/login",{
            username: username,
            password: password
        })
    }
}