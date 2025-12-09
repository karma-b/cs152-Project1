package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.fwjs.parser.FeatherweightJavaScriptBaseVisitor;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;

public class ExpressionBuilderVisitor extends FeatherweightJavaScriptBaseVisitor<Expression>{
    @Override
    public Expression visitProg(FeatherweightJavaScriptParser.ProgContext ctx) {
        List<Expression> stmts = new ArrayList<Expression>();
        for (int i=0; i<ctx.stat().size(); i++) {
            Expression exp = visit(ctx.stat(i));
            if (exp != null) stmts.add(exp);
        }
        return listToSeqExp(stmts);
    }

    @Override
    public Expression visitBareExpr(FeatherweightJavaScriptParser.BareExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitIfThenElse(FeatherweightJavaScriptParser.IfThenElseContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression thn = visit(ctx.block(0));
        Expression els = visit(ctx.block(1));
        return new IfExpr(cond, thn, els);
    }

    @Override
    public Expression visitIfThen(FeatherweightJavaScriptParser.IfThenContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression thn = visit(ctx.block());
        return new IfExpr(cond, thn, null);
    }

    @Override
    public Expression visitInt(FeatherweightJavaScriptParser.IntContext ctx) {
        int val = Integer.valueOf(ctx.INT().getText());
        return new ValueExpr(new IntVal(val));
    }

    @Override
    public Expression visitParens(FeatherweightJavaScriptParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitFullBlock(FeatherweightJavaScriptParser.FullBlockContext ctx) {
        List<Expression> stmts = new ArrayList<Expression>();
        for (int i=1; i<ctx.getChildCount()-1; i++) {
            Expression exp = visit(ctx.getChild(i));
            stmts.add(exp);
        }
        return listToSeqExp(stmts);
    }

    /**
     * Converts a list of expressions to one sequence expression,
     * if the list contained more than one expression.
     */
    private Expression listToSeqExp(List<Expression> stmts) {
        if (stmts.isEmpty()) return null;
        Expression exp = stmts.get(0);
        for (int i=1; i<stmts.size(); i++) {
            exp = new SeqExpr(exp, stmts.get(i));
        }
        return exp;
    }

    @Override
    public Expression visitSimpBlock(FeatherweightJavaScriptParser.SimpBlockContext ctx) {
        return visit(ctx.stat());
    }

    @Override
    public Expression visitWhileLoop(FeatherweightJavaScriptParser.WhileLoopContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression body = visit(ctx.block());
        return new WhileExpr(cond, body);
    }

    @Override
    public Expression visitPrintStatement(FeatherweightJavaScriptParser.PrintStatementContext ctx) {
        return new PrintExpr(visit(ctx.expr()));
    }

    @Override
    public Expression visitEmptyStatement(FeatherweightJavaScriptParser.EmptyStatementContext ctx) {
        return null;
    }

    @Override
    public Expression visitBool(FeatherweightJavaScriptParser.BoolContext ctx) {
        return new ValueExpr(new BoolVal(Boolean.parseBoolean(ctx.BOOL().getText())));
    }

    @Override
    public Expression visitNull(FeatherweightJavaScriptParser.NullContext ctx) {
        return new ValueExpr(new NullVal());
    }

    @Override
    public Expression visitVarRef(FeatherweightJavaScriptParser.VarRefContext ctx) {
        return new VarExpr(ctx.IDS().getText());
    }

    @Override
    public Expression visitVarAssign(FeatherweightJavaScriptParser.VarAssignContext ctx) {
        return new VarDeclExpr(ctx.IDS().getText(), visit(ctx.expr()));
    }

    @Override
    public Expression visitAssignVarIDS(FeatherweightJavaScriptParser.AssignVarIDSContext ctx) {
        return new AssignExpr(ctx.IDS().getText(), visit(ctx.expr()));
    }

    @Override
    public Expression visitFuncDeclare(FeatherweightJavaScriptParser.FuncDeclareContext ctx) {

    }

    @Override
    public Expression visitFuncCall(FeatherweightJavaScriptParser.FuncCallContext ctx) {

    }

    @Override
    public Expression visitMultDivMod(FeatherweightJavaScriptParser.MultDivModContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        Op op;

        if (ctx.op.getType() == FeatherweightJavaScriptParser.MUL) {
            op = Op.MULTIPLY;
        }
        else if (ctx.op.getType() == FeatherweightJavaScriptParser.DIV) {
            op = Op.DIVIDE;
        }
        else if (ctx.op.getType() == FeatherweightJavaScriptParser.MOD) {
            op = Op.MOD;
        }
        else {
            throw new RuntimeException("Invalid Operator!");
        }
        return new BinOpExpr(op, left, right);
    }

    @Override
    public Expression visitAddSub(FeatherweightJavaScriptParser.AddSubContext ctx) {

    }

    @Override
    public Expression visitEquality(FeatherweightJavaScriptParser.EqualityContext ctx) {

    }
}