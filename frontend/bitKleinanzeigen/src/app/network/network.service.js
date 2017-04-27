var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable } from '@angular/core';
import { Http, Request, Headers } from '@angular/http';
var NetworkService = (function () {
    function NetworkService(http) {
        this.http = http;
    }
    NetworkService.prototype.send = function (request) {
        if (request.hasHeaders()) {
            var headers_1 = new Headers();
            var headerArray = request.getHeaders();
            headerArray.forEach(function (header) {
                headers_1.append(header.key, header.value);
            });
            return this.sendRequestWithHeaders(request, headers_1);
        }
        else {
            return this.sendRequest(request);
        }
    };
    NetworkService.prototype.sendRequest = function (request) {
        return this.http.request(new Request({
            method: request.getHttpMethod(),
            url: request.getUrl(),
            body: request.getBody(),
            withCredentials: true
        }));
    };
    NetworkService.prototype.sendRequestWithHeaders = function (request, headers) {
        return this.http.request(new Request({
            method: request.getHttpMethod(),
            url: request.getUrl(),
            body: request.getBody(),
            withCredentials: true,
            headers: headers
        }));
    };
    return NetworkService;
}());
NetworkService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [Http])
], NetworkService);
export { NetworkService };
//# sourceMappingURL=network.service.js.map