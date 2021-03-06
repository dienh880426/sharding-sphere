/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antlr.extractor.impl;

import com.google.common.base.Optional;
import io.shardingsphere.core.parsing.antlr.extractor.CollectionSQLSegmentExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.util.ExtractorUtils;
import io.shardingsphere.core.parsing.antlr.extractor.util.RuleName;
import io.shardingsphere.core.parsing.antlr.sql.segment.column.ColumnDefinitionSegment;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Add column extractor.
 *
 * @author duhongjun
 */
public class AddColumnExtractor implements CollectionSQLSegmentExtractor {
    
    private final ColumnDefinitionExtractor columnDefinitionExtractor = new ColumnDefinitionExtractor();
    
    @Override
    public final Collection<ColumnDefinitionSegment> extract(final ParserRuleContext ancestorNode) {
        Collection<ColumnDefinitionSegment> result = new LinkedList<>();
        for (ParserRuleContext each : ExtractorUtils.getAllDescendantNodes(ancestorNode, RuleName.ADD_COLUMN)) {
            result.addAll(extractAddColumnDefinitions(each));
        }
        return result;
    }
    
    private Collection<ColumnDefinitionSegment> extractAddColumnDefinitions(final ParserRuleContext addColumnNode) {
        Collection<ColumnDefinitionSegment> result = new LinkedList<>();
        for (ParserRuleContext each : ExtractorUtils.getAllDescendantNodes(addColumnNode, RuleName.COLUMN_DEFINITION)) {
            Optional<ColumnDefinitionSegment> columnDefinitionSegment = columnDefinitionExtractor.extract(each);
            if (columnDefinitionSegment.isPresent()) {
                columnDefinitionSegment.get().setAdd(true);
                postExtractColumnDefinition(addColumnNode, columnDefinitionSegment.get());
                result.add(columnDefinitionSegment.get());
            }
        }
        return result;
    }
    
    protected void postExtractColumnDefinition(final ParseTree ancestorNode, final ColumnDefinitionSegment columnDefinitionSegment) {
    }
}
