import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { Route, extract } from '@app/core';
import { MuligheterComponent } from './muligheter.component';

const routes: Routes = Route.withShell([
  { path: 'muligheter', component: MuligheterComponent, data: { title: 'Muligheter i Netcompany' } }
]);

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class MuligheterRoutingModule { }
