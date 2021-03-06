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
package com.github._1c_syntax.bsl.languageserver.diagnostics.reporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github._1c_syntax.bsl.languageserver.diagnostics.FileInfo;
import org.eclipse.lsp4j.Diagnostic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TSLintReporter extends AbstractDiagnosticReporter {

  public static final String KEY = "tslint";

  private static final Logger LOGGER = LoggerFactory.getLogger(TSLintReporter.class.getSimpleName());

  public TSLintReporter(){
    super();
  }

  public TSLintReporter(Path outputDir){
    super(outputDir);
  }

  @Override
  public void report(AnalysisInfo analysisInfo) {
    List<TSLintReportEntry> tsLintReport = new ArrayList<>();
    for (FileInfo fileInfo : analysisInfo.getFileinfos()) {
      for (Diagnostic diagnostic : fileInfo.getDiagnostics()) {
        TSLintReportEntry entry = new TSLintReportEntry(fileInfo.getPath().toString(), diagnostic);
        tsLintReport.add(entry);
      }
    }
    ObjectMapper mapper = new ObjectMapper();

    try {
      File reportFile = new File(outputDir.toFile(), "./bsl-tslint.json");
      mapper.writeValue(reportFile, tsLintReport);
      LOGGER.info("TSLint report saved to {}", reportFile.getAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
