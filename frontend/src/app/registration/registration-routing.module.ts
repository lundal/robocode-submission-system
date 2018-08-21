import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { extract, Logger, Route } from '@app/core';
import { RegistrationComponent } from './registration.component';

const routes: Routes = Route.withShell([
  { path: 'registration', component: RegistrationComponent, data: { title: 'Registration' } }
]);

const log = new Logger('Registration-routing');

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class RegistrationRoutingModule {
  constructor() {
  }
 }
