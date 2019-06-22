/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2019
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package org.github._1c_syntax.bsl.languageserver.diagnostics;

import org.antlr.v4.runtime.tree.ParseTree;
import org.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import org.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticScope;
import org.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import org.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import org.github._1c_syntax.bsl.parser.BSLParser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.MAJOR,
  scope = DiagnosticScope.BSL,
  minutesToFix = 2
)

public class UsingFindElementByStringDiagnostic extends AbstractVisitorDiagnostic {

  private Pattern pattern = Pattern.compile(
    "(НайтиПоНаименованию|FindByDescription|НайтиПоКоду|FindByCode)",
    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

  @Override
  public ParseTree visitMethodCall(BSLParser.MethodCallContext ctx) {
    Matcher matcher = pattern.matcher(ctx.methodName().getText());
    if (matcher.find()) {
      BSLParser.CallParamContext param = ctx.doCall().callParamList().callParam().get(0);
      if (param.children == null || param.getStart().getType() == BSLParser.STRING) {
        diagnosticStorage.addDiagnostic(ctx, getDiagnosticMessage(matcher.group(0)));
      }
    }
    return ctx;
  }

}
