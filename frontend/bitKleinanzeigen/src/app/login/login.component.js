"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var user_model_1 = require("../model/user/user.model");
var login_service_1 = require("./network/login.service");
var security_model_1 = require("../security/security.model");
require("rxjs/add/operator/map");
var LoginComponent = (function () {
    function LoginComponent(login, securityService) {
        this.login = login;
        this.securityService = securityService;
        this.user = new user_model_1.User();
        this.formSubmitted = false;
    }
    LoginComponent.prototype.ngOnInit = function () {
        this.form = new forms_1.FormGroup({
            username: new forms_1.FormControl('', forms_1.Validators.required),
            password: new forms_1.FormControl('', forms_1.Validators.required)
        });
    };
    LoginComponent.prototype.authenticate = function () {
        var _this = this;
        if (this.form.valid) {
            this.login.login(this.user).subscribe(function (response) {
                _this.securityService.setKey('x-author');
                _this.securityService.setSecret('nein');
            });
        }
    };
    LoginComponent.prototype.logout = function () {
        // if (this.securityService.isAuthenticated()) {
        console.log('logout');
        this.login.logout().subscribe(function (res) {
            console.log('logout');
            console.log(res);
            console.log('logout');
        });
        // }
    };
    return LoginComponent;
}());
LoginComponent = __decorate([
    core_1.Component({
        selector: 'login',
        templateUrl: 'login.component.html',
        providers: []
    }),
    __metadata("design:paramtypes", [login_service_1.LoginService, security_model_1.SecurityModel])
], LoginComponent);
exports.LoginComponent = LoginComponent;
//# sourceMappingURL=login.component.js.map