class Auth {
    constructor() {
        this.signedIn = false;
        this.idToken = null;
        this.isFirstTime = false;
        this.email = '';
        this.name = '';
        this.dateJoined = '';
        this.image = '';
        this.desc = '';
    }

    login(cb, tok) {
        this.signedIn = true;
        this.idToken = tok;
        cb();
    }

    logout(cb) {
        this.signedIn = false;
        this.idToken = null;
        cb();
    }

    setIsFirst(isFirst) {
        this.isFirstTime = isFirst;
    }

    isFirstLogin() {
        return this.isFirstTime;
    }

    isAuthenticated() {
        return this.signedIn;
    }

    getCurrUser() {
        return this.idToken;
    }

    getId() {
        return this.id;
    }

    getEmail() {
        return this.email;
    }

    getName() {
        return this.name;
    }

    getImage() {
        return this.image;
    }
}

export default new Auth();