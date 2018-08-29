import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { environment } from '@env/environment';
import { CoreModule } from '@app/core';
import { SharedModule } from '@app/shared';
import { HomeModule } from './home/home.module';
import { AboutModule } from './about/about.module';
import { MuligheterModule } from './muligheter/muligheter.module';
import { OmOssModule } from './om-oss/om-oss.module';
import { LoginModule } from './login/login.module';
import { RegistrationModule } from './registration/registration.module';
import { ReglerModule } from './regler/regler.module';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ToastrModule, Toast } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthInterceptor } from '@app/core/authentication/auth-interceptor.service';
import { AdminModule } from '@app/admin/admin.module';
import { MomentModule } from 'angular2-moment';


@NgModule({
  imports: [
    BrowserModule,
    // ServiceWorkerModule.register('/ngsw-worker.js', {enabled: environment.production}),
    FormsModule,
    HttpModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot(),
    NgbModule.forRoot(),
    ToastrModule.forRoot(),
    CoreModule,
    SharedModule,
    HomeModule,
    AboutModule,
    LoginModule,
    RegistrationModule,
    ReglerModule,
    MuligheterModule,
    OmOssModule,
    AdminModule,
    MomentModule,
    AppRoutingModule
  ],
  declarations: [AppComponent],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
