import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthenticationTokenService} from './authenticationtoken.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authenticationTokenService: AuthenticationTokenService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authenticationTokenService.isAuthenticated()) {
      const credentials = this.authenticationTokenService.credentials;
      const authReq = req.clone({headers: req.headers.set('Authorization', credentials.jwt)});
      return next.handle(authReq);
    }

    return next.handle(req);
  }
}
