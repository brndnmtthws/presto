/*
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
 */
package com.facebook.presto.sql.planner.plan;

import com.facebook.presto.sql.planner.Symbol;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;

@Immutable
public final class RowNumberNode
        extends PlanNode
{
    private final PlanNode source;
    private final List<Symbol> partitionBy;
    private final Optional<Integer> maxRowCountPerPartition;
    private final Symbol rowNumberSymbol;

    @JsonCreator
    public RowNumberNode(
            @JsonProperty("id") PlanNodeId id,
            @JsonProperty("source") PlanNode source,
            @JsonProperty("partitionBy") List<Symbol> partitionBy,
            @JsonProperty("rowNumberSymbol") Symbol rowNumberSymbol,
            @JsonProperty("maxRowCountPerPartition") Optional<Integer> maxRowCountPerPartition)
    {
        super(id);

        checkNotNull(source, "source is null");
        checkNotNull(partitionBy, "partitionBy is null");
        checkNotNull(rowNumberSymbol, "rowNumberSymbol is null");
        checkNotNull(maxRowCountPerPartition, "maxRowCountPerPartition is null");

        this.source = source;
        this.partitionBy = ImmutableList.copyOf(partitionBy);
        this.rowNumberSymbol = rowNumberSymbol;
        this.maxRowCountPerPartition = maxRowCountPerPartition;
    }

    @Override
    public List<PlanNode> getSources()
    {
        return ImmutableList.of(source);
    }

    @Override
    public List<Symbol> getOutputSymbols()
    {
        return ImmutableList.copyOf(concat(source.getOutputSymbols(), ImmutableList.of(rowNumberSymbol)));
    }

    @JsonProperty
    public PlanNode getSource()
    {
        return source;
    }

    @JsonProperty
    public List<Symbol> getPartitionBy()
    {
        return partitionBy;
    }

    @JsonProperty
    public Symbol getRowNumberSymbol()
    {
        return rowNumberSymbol;
    }

    @JsonProperty
    public Optional<Integer> getMaxRowCountPerPartition()
    {
        return maxRowCountPerPartition;
    }

    @Override
    public <C, R> R accept(PlanVisitor<C, R> visitor, C context)
    {
        return visitor.visitRowNumber(this, context);
    }
}
