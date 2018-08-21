import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { OmOssRoutingModule } from './om-oss-routing.module';
import { OmOssComponent } from './om-oss.component';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    OmOssRoutingModule
  ],
  declarations: [
    OmOssComponent
  ]
})
export class OmOssModule { }
