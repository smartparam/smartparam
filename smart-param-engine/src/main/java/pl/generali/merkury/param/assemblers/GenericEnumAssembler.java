package pl.generali.merkury.param.assemblers;

import pl.generali.merkury.param.core.assembler.Assembler;
import pl.generali.merkury.param.core.context.ParamContext;
import pl.generali.merkury.param.core.type.AbstractHolder;

/**
 * Klasa dostarcza {@link Assembler}, ktory zamienia kod
 * na obiekt typu <tt>Enum</tt> odpowiedniej klasy.
 * <p>
 * Metoda assemblera pobiera klase Enuma z kontekstu ({@link ParamContext#getResultClass()}),
 * a kod Enuma z przekazanego holdera ({@link AbstractHolder#getString()).
 * Nastepnie zwraca obiekt enum zadanej klasy o zadanym kodzie.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class GenericEnumAssembler {

    /**
     * Zwraca enum typu <tt>ctx.getResultClass</tt> o kodzie zgodnym z wartoscia <tt>value</tt> (case sensitive).
     *
     * @param value holder interpretowany jako kod enuma
     * @param ctx   kontekst, w ktorym przechowywana jest klasa enuma
     * @return enum klasy T o nazwie rownej wartosci value.getString()
     */
    @Assembler
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum<?> findEnum(AbstractHolder value, ParamContext ctx) {
        String code = value.getString();
        return code != null ? Enum.valueOf((Class<? extends Enum>) ctx.getResultClass(), code) : null;
    }
}
