import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { Route, extract } from '@app/core';
import { OmOssComponent } from './om-oss.component';

const routes: Routes = Route.withShell([
  { path: 'om-oss', component: OmOssComponent, data: { title: 'Om oss' } }
]);

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class OmOssRoutingModule { }
