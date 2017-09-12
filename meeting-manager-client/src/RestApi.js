import request from "superagent"

class EmployeeApi {

    constructor(baseurl){
        this.url = baseurl + "/employees"
    }

    getone(id){
        return request.get(this.url+"/"+id);
    }

    create(employee){
        return request.post(this.url+"/", employee);
    }

    update(employee){
        return request.put(this.url+"/"+employee.id, employee);
    }

    listall(){
        return request.get(this.url+"/");
    }
}

class RestApi {
    constructor(url){
        this.url = url;
        this.employeeApi = new EmployeeApi(url)
    }
}

const api = new RestApi("http://192.168.1.25:8080");
export default api;

